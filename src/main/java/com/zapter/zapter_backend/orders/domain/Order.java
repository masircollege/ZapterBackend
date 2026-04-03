package com.zapter.zapter_backend.orders.domain;

import com.zapter.zapter_backend.payment.domain.Payment;
import com.zapter.zapter_backend.user.domain.User;
import com.zapter.zapter_backend.orders.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User orderUser;

	@Column(columnDefinition = "varchar(255) default 'PENDING'", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(nullable = false)
	private BigDecimal amount;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private LocalDateTime dispatchDate;

	@Column(nullable = false)
	private LocalDateTime deliveryDate;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Payment payment;

	@OneToMany(mappedBy = "orders")
	private Set<OrderProduct> orderProduct = new HashSet<>();

}
 