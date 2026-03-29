package com.zapter.zapter_backend.product.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.zapter.zapter_backend.cart.domain.CartProduct;
import com.zapter.zapter_backend.measurement.domain.ProductMeasurement;
import com.zapter.zapter_backend.order.domain.OrderProduct;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Column(nullable = false)
	private String name;

	private String color;

	private String description;

	@Column(nullable = false)
	private BigDecimal price;

//	@Enumerated(EnumType.STRING) // This will be implemented once we shift to Multi-Vendor model.
//	@Column(name = "approval_status", nullable = false, columnDefinition = "VARCHAR(20) default 'PENDING'")
//	private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name = "model_id",nullable = false)
	private Model model;

	@JsonManagedReference
	@OneToMany(mappedBy = "measurementProduct",fetch = FetchType.LAZY)
	private List<ProductMeasurement> productMeasurement = new ArrayList<>();

	@OneToMany(mappedBy = "prodFeature")
	private Set<ProductKeyFeature> productKeyFeatures = new HashSet<>();

	@OneToMany(mappedBy = "cartProduct")
	private Set<CartProduct> cartProducts = new HashSet<>();

	@OneToMany(mappedBy = "orderProduct")
	private Set<OrderProduct> orders = new HashSet<>();

	@OneToMany(mappedBy = "reviewProduct")
	private List<Review> prodReview = new ArrayList<>() ;

	@OneToMany(mappedBy = "detailsProduct")
	private List<ProductDetails> productDetails = new ArrayList<>();

	@OneToMany(mappedBy = "inventoryProduct")
	private List<Inventory> inventory = new ArrayList<>();

}


//	@ManyToOne
//	@JoinColumn(name = "seller_id")
//	private Seller productSeller;
