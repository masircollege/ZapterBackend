package com.zapter.zapter_backend.product.repository;

import com.zapter.zapter_backend.product.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByInventoryProductId(Long inventoryProductId);
}
