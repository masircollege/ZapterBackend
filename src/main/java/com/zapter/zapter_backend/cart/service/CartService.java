package com.zapter.zapter_backend.cart.service;

import com.zapter.zapter_backend.cart.domain.Cart;
import com.zapter.zapter_backend.cart.domain.CartProduct;
import com.zapter.zapter_backend.cart.dto.*;
import com.zapter.zapter_backend.cart.dto.interfaces.CartByUserDto;
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
        cart.setCartUser(userRepository.findById(userId).get());
        return cartRepository.save(cart);
    }

    public void addToCart(Long userId, Long productId) {
        transactionTemplate.execute(status -> {
            try {
                if (cartRepository.findCartByUserId(userId) == null) {
                    Cart cart = createCart(userId);
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.setCarts(cart);
                    cartProduct.setCartProduct(productRepository.findById(productId).get());
                    cartProduct.setQuantity(1);
                    cartProductRepository.save(cartProduct);
                } else {
                    Cart cart = cartRepository.findById(cartRepository.findCartByUserId(userId)).get();
                    if (cartProductRepository.existsByProductIdAndCartId(productId, cart.getId())) {
                        cartProductRepository.updateQuantity(productId,cart.getId());
                    } else {
                        CartProduct cartProduct = new CartProduct();
                        cartProduct.setCarts(cart);
                        cartProduct.setCartProduct(productRepository.findById(productId).get());
                        cartProduct.setQuantity(1);
                        cartProductRepository.save(cartProduct);
                    }
                }
                return null;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
        });
    }

    public CartResponse getCartByUserId(Long userId) {
        try {
            CartByUserDto cart = cartRepository.findCartDtoDetailsByUserId(userId);
            Set<CartProductDetails> rawProducts = cartProductRepository.findCartProductByCartId(cart.getCartId());
            Set<CartProductDetailsDto> cartProducts = rawProducts.stream()
                    .map(product -> new CartProductDetailsDto(
                            product.getProductId(),
                            product.getProductName(),
                            product.getColor(),
                            product.getPrice(),
                            product.getQuantity(),
                            product.getStockStatus()
                    )).collect(Collectors.toSet());
            List<BigDecimal> priceList = new ArrayList<>();
            BigDecimal totalPrice = new BigDecimal(0);
            cartProducts.forEach(cp -> {
                priceList.add(cp.getPrice().multiply(new BigDecimal(cp.getQuantity())));
            });
            for (BigDecimal price : priceList) {
                totalPrice = totalPrice.add(price);
            }
            return new CartResponse(
                    cart.getCartId(),
                    cart.getUserId(),
                    cartProducts,
                    totalPrice
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
