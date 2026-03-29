package com.zapter.zapter_backend.product.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
@Table(name = "brands")
public class Brand{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "brand_name", nullable = false)
	private String name;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@JsonManagedReference
	@OneToMany(mappedBy = "brand")
	private List<Product> brandProduct = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "brandModel")
	private List<Model> model = new ArrayList<>();
}
