package com.zapter.zapter_backend.user.domain;

import com.zapter.zapter_backend.orders.domain.Order;
import com.zapter.zapter_backend.product.domain.Review;
import com.zapter.zapter_backend.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "phone_no", nullable = false, length = 10)
	private String phoneNumber;

	@Column(name = "country_code", nullable = false)
	private String countryCode;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "VARCHAR(20) default 'USER'")
	private Role role = Role.USER;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Email(message = "Invalid email.")
	@Column(name = "email")
	private String email;

	@Column
	private String address;
	@Column
	private String pincode;
	@Column
	private String district;
	@Column
	private String city;
	@Column
	private String state;

	@OneToMany(mappedBy = "orderUser")
	private List<Order> userOrder = new ArrayList<>();

	@OneToMany(mappedBy = "reviewUser")
	private List<Review> useReview = new ArrayList<>();

	@OneToMany(mappedBy = "daUser")
	private List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new SimpleGrantedAuthority(role.toString()));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return phoneNumber;
	}

}
