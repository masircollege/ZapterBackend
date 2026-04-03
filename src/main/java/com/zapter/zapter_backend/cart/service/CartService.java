package com.zapter.zapter_backend.cart.service;

import com.zapter.zapter_backend.cart.domain.Cart;
import com.zapter.zapter_backend.cart.domain.CartProduct;
import com.zapter.zapter_backend.cart.dto.*;
import com.zapter.zapter_backend.cart.dto.interfaces.CartProductDetails;
import com.zapter.zapter_backend.cart.repository.CartProductRepository;
import com.zapter.zapter_backend.cart.repository.CartRepository;
import com.zapter.zapter_backend.product.repository.ProductRepository;
import com.zapter.zapter_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;
    private final TransactionTemplate transactionTemplate;

    public CartService(
            CartRepository cartRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            CartProductRepository cartProductRepository, TransactionTemplate transactionTemplate) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
        this.transactionTemplate = transactionTemplate;
    }

    private Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUser(userRepository.findById(userId).get());
        return cartRepository.save(cart);
    }

    public void updateCartAmount(Cart cart) {
        transactionTemplate.execute(status -> {
            try {
                Set<CartProductDetails> rawProducts = cartProductRepository.findCartProductByCartId(cart.getId());
                List<BigDecimal> priceList = new ArrayList<>();
                BigDecimal totalPrice = new BigDecimal(0);
                rawProducts.forEach(cp -> {
                    priceList.add(cp.getPrice().multiply(new BigDecimal(cp.getQuantity())));
                });
                for (BigDecimal price : priceList) {
                    totalPrice = totalPrice.add(price);
                }
                cart.setTotalPrice(totalPrice);
                cartRepository.save(cart);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public void addToCart(Long userId, Long productId) {
        transactionTemplate.execute(status -> {
            try {
                if (cartRepository.findByUserId(userId).isEmpty()) {
                    Cart cart = createCart(userId);
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setCarts(cart);
                    cartProduct.setCartProduct(productRepository.findById(productId).get());
                    cartProduct.setQuantity(1);
                    cartProductRepository.save(cartProduct);
                    updateCartAmount(cart);
                } else {
                    Cart cart = cartRepository.findByUserId(userId).get();
                    if (cartProductRepository.existsByProductIdAndCartId(productId, cart.getId())) {
                        cartProductRepository.updateQuantity(productId,cart.getId(),1);
                        updateCartAmount(cart);
                    } else {
                        CartProduct cartProduct = new CartProduct();
                        cartProduct.setCarts(cart);
                        cartProduct.setCartProduct(productRepository.findById(productId).get());
                        cartProduct.setQuantity(1);
                        cartProductRepository.save(cartProduct);
                        updateCartAmount(cart);
                    }
                }
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
        });
    }

    public void removeFromCart(Long userId, Long productId) {
        transactionTemplate.execute(status -> {
            try {
                Optional<Cart> cart = cartRepository.findByUserId(userId);
                CartProduct cartProduct = cartProductRepository.findByCartProductIdAndCartsId(productId,cart.get().getId());
                if (cartProduct.getQuantity() == 1) {
                    cartProductRepository.delete(cartProduct);
                } else {
                    cartProductRepository.updateQuantity(productId,cart.get().getId(), -1);
                }
                updateCartAmount(cart.get());
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public CartResponse getCartByUserId(Long userId) {
        try {
            Optional<Cart> cart = cartRepository.findByUserId(userId);
            Set<CartProductDetails> rawProducts = cartProductRepository.findCartProductByCartId(cart.get().getId());
            Set<CartProductDetailsDto> cartProducts = rawProducts.stream()
                    .map(product -> new CartProductDetailsDto(
                            product.getProductId(),
                            product.getProductName(),
                            product.getColor(),
                            product.getPrice(),
                            product.getQuantity(),
                            product.getStockStatus()
                    )).collect(Collectors.toSet());
            return new CartResponse(
                    cart.get().getId(),
                    cart.get().getUser().getId(),
                    cartProducts,
                    cart.get().getTotalPrice()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
