package com.zapter.zapter_backend.cart.repository;

import com.zapter.zapter_backend.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

//    @Query(value = "SELECT cart FROM cart WHERE user_id = :user_id",nativeQuery = true)
    Optional<Cart> findByUserId(Long userId);

}
