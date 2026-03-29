//package com.zapter.zapter_backend.user.domain;
//
//import com.zapter.zapter_backend.product.domain.Category;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@Table(name = "seller_category")
//public class SellerCategory {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "seller_id")
//    private Seller categorySeller;
//
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category sellerCategory;
//
//}
