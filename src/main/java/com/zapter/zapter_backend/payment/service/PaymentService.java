package com.zapter.zapter_backend.payment.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.zapter.zapter_backend.cart.domain.Cart;
import com.zapter.zapter_backend.cart.domain.CartProduct;
import com.zapter.zapter_backend.cart.repository.CartRepository;
import com.zapter.zapter_backend.orders.domain.Order;
import com.zapter.zapter_backend.orders.domain.OrderProduct;
import com.zapter.zapter_backend.orders.enums.Status;
import com.zapter.zapter_backend.orders.repository.OrderProductRepository;
import com.zapter.zapter_backend.orders.repository.OrderRepository;
import com.zapter.zapter_backend.payment.domain.Payment;
import com.zapter.zapter_backend.payment.dto.*;
import com.zapter.zapter_backend.payment.enums.PaymentMode;
import com.zapter.zapter_backend.payment.enums.PaymentStatus;
import com.zapter.zapter_backend.payment.repository.PaymentRepository;
import com.zapter.zapter_backend.product.domain.Inventory;
import com.zapter.zapter_backend.product.repository.InventoryRepository;
import com.zapter.zapter_backend.user.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class PaymentService {

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    private static final BigDecimal COD_LIMIT = new BigDecimal("50000");

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final PaymentRepository paymentRepository;
    private final InventoryRepository inventoryRepository;

    public PaymentService(
            CartRepository cartRepository,
            UserRepository userRepository,
            OrderRepository orderRepository,
            OrderProductRepository orderProductRepository,
            PaymentRepository paymentRepository,
            InventoryRepository inventoryRepository
    ) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.paymentRepository = paymentRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * STEP 1 of online flow.
     * Creates a Razorpay Order and returns the orderID + publicKey to the frontend.
     * The frontend uses these to open the Razorpay Checkout popup.
     */
    public InitiatePaymentResponse initiateOnlinePayment(InitiatePaymentRequest request) {
        try {
            Cart cart = cartRepository.findById(request.cartId())
                    .orElseThrow(() -> new RuntimeException("Cart not found: " + request.cartId()));

            BigDecimal totalAmount = cart.getTotalPrice(); // ensure this method exists on Cart

            // Razorpay expects amount in PAISE (1 INR = 100 paise)
            int amountInPaise = totalAmount.multiply(BigDecimal.valueOf(100)).intValue();

            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "zapter_cart_" + cart.getId());

            com.razorpay.Order razorpayOrder = client.orders.create(orderRequest);
            String razorpayOrderId = razorpayOrder.get("id");

            return new InitiatePaymentResponse(razorpayOrderId, totalAmount, "INR", keyId);

        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    /**
     * STEP 2 of online flow.
     * Verifies the HMAC-SHA256 signature that Razorpay sends after payment.
     * If valid → creates the Order + Payment in DB → updates Inventory → clears Cart.
     * <p>
     * Signature verification formula (from Razorpay docs):
     * HMAC_SHA256(razorpay_order_id + "|" + razorpay_payment_id, key_secret)
     */
    @Transactional
    public PaymentOrderCreatedResponse verifyAndCreateOrder(VerifyPaymentRequest request) {
        // 1. Verify signature
        boolean isValid = verifySignature(
                request.razorpayOrderId(),
                request.razorpayPaymentId(),
                request.razorpaySignature()
        );

        if (!isValid) {
            throw new RuntimeException("Payment verification failed. Invalid signature.");
        }

        // 2. Load cart
        Cart cart = cartRepository.findById(request.cartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // 3. Create & persist Order
        Order order = buildOrder(request.userId(), cart, request.deliveryAddress());
        orderRepository.save(order);

        // 4. Persist OrderProducts + reduce Inventory
        persistOrderProductsAndUpdateInventory(order, cart);

        // 5. Create Payment record
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMode(PaymentMode.UPI); // Razorpay covers both UPI and Netbanking
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setRazorpayOrderId(request.razorpayOrderId());
        payment.setRazorpayPaymentId(request.razorpayPaymentId());
        payment.setRazorpaySignature(request.razorpaySignature());
        paymentRepository.save(payment);

        // 6. Clear the cart after successful order
        cart.getCartProducts().clear();
        cartRepository.save(cart);

        return new PaymentOrderCreatedResponse(order.getId(), "Order placed successfully!");
    }

    /**
     * COD flow — single step.
     * Validates amount < ₹50,000 then directly creates Order + Payment.
     */
    @Transactional
    public PaymentOrderCreatedResponse createCODOrder(CODOrderRequest request) {
        Cart cart = cartRepository.findById(request.cartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getTotalPrice().compareTo(COD_LIMIT) >= 0) {
            throw new RuntimeException(
                    "Cash on Delivery is only available for orders below ₹50,000. " +
                            "Please choose an online payment method."
            );
        }

        // Create & persist Order
        Order order = buildOrder(request.userId(), cart, request.deliveryAddress());
        orderRepository.save(order);

        // Persist OrderProducts + reduce Inventory
        persistOrderProductsAndUpdateInventory(order, cart);

        // Create Payment record — status stays PENDING until delivery
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMode(PaymentMode.COD);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);

        // Clear cart
        cart.getCartProducts().clear();
        cartRepository.save(cart);

        return new PaymentOrderCreatedResponse(order.getId(), "COD order placed successfully!");
    }

    // ─────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────

    private Order buildOrder(Long userId, Cart cart, String deliveryAddress) {
        Order order = new Order();
        order.setOrderUser(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
        order.setAmount(cart.getTotalPrice());
        order.setAddress(deliveryAddress);
        order.setStatus(Status.PENDING);
        order.setDispatchDate(LocalDateTime.now().plusDays(1));
        order.setDeliveryDate(LocalDateTime.now().plusDays(4));
        return order;
    }

    private void persistOrderProductsAndUpdateInventory(Order order, Cart cart) {
        for (CartProduct cartProduct : cart.getCartProducts()) {
            // Create OrderProduct
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderProduct(cartProduct.getCartProduct());
            orderProduct.setOrders(order);
            orderProduct.setQuantity(cartProduct.getQuantity());
            orderProductRepository.save(orderProduct);

            // Reduce inventory stock
            Inventory inventory = inventoryRepository
                    .findByInventoryProductId(cartProduct.getCartProduct().getId()).orElseThrow(() -> new RuntimeException(
                            "Inventory not found for product: " + cartProduct.getCartProduct().getId()
                    ));
            int newQty = inventory.getQuantity() - cartProduct.getQuantity();
            if (newQty < 0) {
                throw new RuntimeException(
                        "Insufficient stock for: " + cartProduct.getCartProduct().getName()
                );
            }
            inventory.setQuantity(newQty);
            inventoryRepository.save(inventory);
        }
    }

    /**
     * HMAC-SHA256 verification per Razorpay spec.
     * razorpay_order_id + "|" + razorpay_payment_id → signed with key_secret
     */
    private boolean verifySignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"
            );
            mac.init(secretKey);
            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String generated = HexFormat.of().formatHex(hash);
            return generated.equals(signature);
        } catch (Exception e) {
            throw new RuntimeException("Signature verification error", e);
        }
    }
}