package com.zapter.zapter_backend.user.domain;

import com.zapter.zapter_backend.product.domain.Inventory;
import com.zapter.zapter_backend.product.domain.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendor")
@Getter
@Setter
@NoArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "vendor_name")
    private String name;

    @Column(name = "vendor_address")
    private String address;

    @OneToMany(mappedBy = "vendor")
    private List<Warehouse> warehouses = new ArrayList<>();

    @OneToMany(mappedBy = "inventoryVendor")
    private List<Inventory> vendorProducts = new ArrayList<>();

}
