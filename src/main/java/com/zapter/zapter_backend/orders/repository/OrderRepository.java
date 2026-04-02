package com.zapter.zapter_backend.orders.repository;

import com.zapter.zapter_backend.orders.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
