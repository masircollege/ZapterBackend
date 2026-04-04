package com.zapter.zapter_backend.user.repository;

import com.zapter.zapter_backend.user.domain.User;
import com.zapter.zapter_backend.user.service.VendorService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<UserDetails> findByPhoneNumber(String phoneNumber);
}
