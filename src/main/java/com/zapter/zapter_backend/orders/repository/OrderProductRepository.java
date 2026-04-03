package com.zapter.zapter_backend.orders.repository;

import com.zapter.zapter_backend.orders.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>{

}
