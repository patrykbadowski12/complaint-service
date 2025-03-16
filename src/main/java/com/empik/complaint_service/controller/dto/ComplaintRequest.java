package com.empik.complaint_service.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComplaintRequest(
        @NotBlank(message = "Description cannot be blank")
        @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
        String description,

        @Valid
        Reporter reportedBy,

        @NotNull(message = "Product ID cannot be null")
        Long productId
) {}
