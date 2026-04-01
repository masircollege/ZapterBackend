package com.zapter.zapter_backend.cart.domain;

import com.zapter.zapter_backend.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
		name = "cart_product",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"cart_id","product_id"})
		}
)
public class CartProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart carts;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product cartProduct;
}
