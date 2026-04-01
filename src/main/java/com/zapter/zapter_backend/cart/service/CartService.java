package com.zapter.zapter_backend.cart.service;

import com.zapter.zapter_backend.cart.domain.Cart;
import com.zapter.zapter_backend.cart.domain.CartProduct;
import com.zapter.zapter_backend.cart.dto.CartByUserDto;
import com.zapter.zapter_backend.cart.dto.CartProductDto;
import com.zapter.zapter_backend.cart.dto.CartResponse;
import com.zapter.zapter_backend.cart.repository.CartProductRepository;
import com.zapter.zapter_backend.cart.repository.CartRepository;
import com.zapter.zapter_backend.product.repository.ProductRepository;
import com.zapter.zapter_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public CartService(
            CartRepository cartRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartProductRepository = cartProductRepository;
    }

    private Cart createCart(Long userId){
        Cart cart = new Cart();
        cart.setCartUser(userRepository.findById(userId).get());
        return cartRepository.save(cart);
    }

    public void addToCart(Long userId,Long productId){
        try {
            if (cartRepository.findCartByUserId(userId)==null){
                Cart cart = createCart(userId);
                CartProduct cartProduct = new CartProduct();
                cartProduct.setCarts(cart);
                cartProduct.setCartProduct(productRepository.findById(productId).get());
                cartProductRepository.save(cartProduct);
            }
            else {
                Cart cart = cartRepository.findById(cartRepository.findCartByUserId(userId)).get();
                CartProduct cartProduct = new CartProduct();
                cartProduct.setCarts(cart);
                cartProduct.setCartProduct(productRepository.findById(productId).get());
                cartProductRepository.save(cartProduct);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CartResponse getCartByUserId(Long userId) {
        try {
            CartByUserDto cart = cartRepository.findCartDtoDetailsByUserId(userId);
            Set<CartProductDto> cartProducts = cartRepository.findCartProductByCartId(cart.getCartId());
            return new CartResponse(
                    cart.getCartId(),
                    cart.getUserId(),
                    cartProducts,
                    cartRepository.findTotalPriceByCartId(cart.getCartId())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
