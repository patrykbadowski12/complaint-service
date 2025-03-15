package com.empik.complaint_service.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Reporter(
        @NotBlank(message = "Reporter name cannot be blank")
        String name,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email
) {}