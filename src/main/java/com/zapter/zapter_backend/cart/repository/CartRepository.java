package com.zapter.zapter_backend.cart.repository;

import com.zapter.zapter_backend.cart.domain.Cart;
import com.zapter.zapter_backend.cart.dto.CartByUserDto;
import com.zapter.zapter_backend.cart.dto.CartProductDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT id FROM cart WHERE user_id = :user_id",nativeQuery = true)
    Long findCartByUserId(@Param("user_id") Long userId);

    @Query(value = "SELECT id AS cart_id,user_id FROM cart WHERE user_id = :user_id", nativeQuery = true)
    CartByUserDto findCartDtoDetailsByUserId(@Param("user_id")Long userId);

    @Query(
            value =
                    "SELECT p.id AS product_id,p.name AS product_name,p.color,p.price,p.stock_status " +
                            "FROM cart_product cp INNER JOIN products p " +
                            "ON cp.product_id = p.id WHERE cp.cart_id = :cart_id",
            nativeQuery = true
    )
    Set<CartProductDto> findCartProductByCartId(@Param("cart_id") Long cartId);

    @Query(value = "SELECT SUM(price) FROM products WHERE id IN (SELECT product_id FROM cart_product WHERE cart_id = :cart_id)", nativeQuery = true)
    BigDecimal findTotalPriceByCartId(@Param("cart_id") Long cartId);

}
