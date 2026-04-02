package com.zapter.zapter_backend.orders.repository;

import com.zapter.zapter_backend.orders.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long>{

}
