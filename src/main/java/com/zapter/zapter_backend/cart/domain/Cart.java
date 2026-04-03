package com.zapter.zapter_backend.cart.domain;

import com.zapter.zapter_backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cart")
public class Cart{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "user_id" ,nullable = false)
	private User user;

	@Column(nullable = false)
	private BigDecimal totalPrice = BigDecimal.ZERO;
	
	@OneToMany(mappedBy = "carts")
	private Set<CartProduct> cartProducts = new HashSet<>();
	
}
