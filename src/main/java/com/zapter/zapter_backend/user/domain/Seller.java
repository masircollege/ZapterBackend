//package com.zapter.zapter_backend.user.domain;
//
//import com.zapter.zapter_backend.product.domain.Category;
//import com.zapter.zapter_backend.product.domain.Inventory;
//import com.zapter.zapter_backend.product.domain.Product;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@Table(name = "seller")
//public class Seller {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "user_id", referencedColumnName = "id")
//	private User user;
//
//	@OneToMany(mappedBy = "pickupAddressSeller")
//	private List<PickupAddress> pickupAddresses = new ArrayList<>();
//
//	@OneToMany(mappedBy = "inventorySeller")
//	private List<Inventory> inventories = new ArrayList<>();
//
//	@OneToMany(mappedBy = "productSeller")
//	private List<Product> products = new ArrayList<>();
//
//	@OneToMany(mappedBy = "categorySeller") // M-M
//	private Set<SellerCategory> sellerCategories = new HashSet<>();
//}
