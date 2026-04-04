package com.zapter.zapter_backend.user.dto.user;

public record UserResponse(
		Long id,
		String firstName,
		String lastName,
		String address
		) {}
