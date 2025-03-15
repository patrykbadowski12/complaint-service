package com.empik.complaint_service.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateComplaintRequest(@NotBlank(message = "Description cannot be blank") String description) { }
