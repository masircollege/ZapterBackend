package com.zapter.zapter_backend.product.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "model")
public class Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private String name;

	@CreationTimestamp
	@Column(name = "created_at",updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brandModel;

	@JsonManagedReference
	@OneToMany(mappedBy = "model")
	private List<Product> products = new ArrayList<>();
	
}
