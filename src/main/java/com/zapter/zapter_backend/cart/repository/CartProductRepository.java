package com.zapter.zapter_backend.cart.repository;

import com.zapter.zapter_backend.cart.domain.CartProduct;
import com.zapter.zapter_backend.cart.dto.CartProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long>{

    @Query(
            value =
                    "SELECT p.id AS product_id,p.name AS product_name,p.color,p.price,p.stock_status, cp.quantity " +
                            "FROM cart_product cp INNER JOIN products p " +
                            "ON cp.product_id = p.id WHERE cp.cart_id = :cart_id",
            nativeQuery = true
    )
    Set<CartProductDetails> findCartProductByCartId(@Param("cart_id") Long cartId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM cart_product WHERE product_id = :product_id)", nativeQuery = true)
    boolean existsByProductId(@Param("product_id") Long productId);

    @Modifying
    @Query(value = "UPDATE cart_product SET quantity = quantity + 1 WHERE product_id = :product_id", nativeQuery = true)
    void updateQuantity(@Param("product_id") Long productId);

}
