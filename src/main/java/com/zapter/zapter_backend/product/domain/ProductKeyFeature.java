package com.zapter.zapter_backend.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "product_key_feature",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id","feature_id"})
        }
)
public class ProductKeyFeature { // M-M

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product prodFeature;

    @ManyToOne
    @JoinColumn(name = "feature_id")
    private KeyFeatures keyFeatures;
}
