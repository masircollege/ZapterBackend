package com.zapter.zapter_backend.user.dto.user;

import com.zapter.zapter_backend.user.interfaces.Accounts;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewUser(
		@Pattern(regexp = "^[a-zA-Z]+$", message = "Digits and special characters are not allowed.")
		@NotBlank
		String firstName,

		@Pattern(regexp = "^[a-zA-Z]+$", message = "Digits and special characters are not allowed.")
		@NotBlank
		String lastName,

		@NotBlank
		String countryCode,

		@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^+-=&*])[a-zA-Z0-9!@#$%+-=^&*]{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
		@NotBlank
		String password
) implements Accounts {
}
