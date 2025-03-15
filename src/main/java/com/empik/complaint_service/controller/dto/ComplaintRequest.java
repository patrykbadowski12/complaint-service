package com.empik.complaint_service.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComplaintRequest(
        @NotBlank(message = "Description cannot be blank")
        String description,

        @Valid
        Reporter reportedBy,

        @NotNull(message = "Product ID cannot be null")
        Long productId
) {}

