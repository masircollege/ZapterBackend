package com.zapter.zapter_backend.user.domain;

import com.zapter.zapter_backend.product.domain.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "pickup_address")
public class PickupAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    private String pincode;

    private String city;

    private String district;

    private String state;
}
