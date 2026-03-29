package com.zapter.zapter_backend.product.domain;

import com.zapter.zapter_backend.product.enums.StockStatus;
import com.zapter.zapter_backend.user.domain.Vendor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(name = "first_batch", nullable = false, updatable = false)
	private LocalDateTime firstBatch;

	@UpdateTimestamp
	@Column(name = "latest_batch", nullable = false)
	private LocalDateTime latestBatch;

	@Column(nullable = false)
	private Integer quantity;

	@Column(name = "min_count",nullable = false)
	private Integer minimumCount;

//	@Column(nullable = false, columnDefinition = "varchar(255) default 'AVAILABLE'")
//	@Enumerated(EnumType.STRING)
//	private StockStatus stockStatus;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product inventoryProduct;

//	@ManyToOne
//	@JoinColumn(name = "seller_id", nullable = false)
//	private Seller inventorySeller;

	@ManyToOne
	@JoinColumn(name = "warehouse_id", nullable = false)
	private Warehouse inventoryWarehouse;

	@ManyToOne
	@JoinColumn(name = "vendor_id", nullable = false)
	private Vendor inventoryVendor;

}
