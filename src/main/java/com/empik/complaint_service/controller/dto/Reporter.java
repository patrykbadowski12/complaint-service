package com.empik.complaint_service.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Reporter(
        @NotBlank(message = "Reporter name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "Invalid email format"
        )
        String email
) {}