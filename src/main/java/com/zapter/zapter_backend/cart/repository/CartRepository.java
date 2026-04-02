package com.zapter.zapter_backend.cart.repository;

import com.zapter.zapter_backend.cart.domain.Cart;
import com.zapter.zapter_backend.cart.dto.interfaces.CartByUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT id FROM cart WHERE user_id = :user_id",nativeQuery = true)
    Long findCartByUserId(@Param("user_id") Long userId);

    @Query(value = "SELECT id AS cart_id,user_id FROM cart WHERE user_id = :user_id", nativeQuery = true)
    CartByUserDto findCartDtoDetailsByUserId(@Param("user_id")Long userId);

}
