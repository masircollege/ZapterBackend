package com.zapter.zapter_backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserInputPhone(
        @Pattern(
                regexp = "^\\+[1-9]\\d{1,14}$",
                message = "Enter valid phone number in E.164 format (e.g., +919876543210)."
        )
        @NotBlank(message = "Please provide phone number.")
        String phoneNumber
) {
}
