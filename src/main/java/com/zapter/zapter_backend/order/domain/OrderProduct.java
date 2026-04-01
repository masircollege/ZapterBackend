package com.zapter.zapter_backend.order.domain;

import com.zapter.zapter_backend.product.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
		name = "order_product",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"product_id","order_id"})
		}
)
public class OrderProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product orderProduct;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order orders;
	
}
