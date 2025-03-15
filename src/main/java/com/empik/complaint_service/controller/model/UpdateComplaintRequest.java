package com.empik.complaint_service.controller.model;

import jakarta.validation.constraints.NotBlank;

public record UpdateComplaintRequest(@NotBlank(message = "Description cannot be blank") String description) { }
