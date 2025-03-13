package com.empik.complaint_service.controller.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ComplaintResponse(
        Long complaintId,
        Long productId,
        String description,
        LocalDateTime createdAt,
        String reporterName,
        String reporterEmail,
        String reporterCountry,
        int reportCount
) {
}
