package com.empik.complaint_service.service;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.ComplaintResponse;
import com.empik.complaint_service.repository.ComplaintEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComplaintMapper {

    static ComplaintEntity createComplaintEntity(final ComplaintRequest request, final String country) {
        return ComplaintEntity.builder()
                .productId(request.productId())
                .description(request.description())
                .reporterName(request.reportedBy().name())
                .reporterEmail(request.reportedBy().email())
                .reporterCountry(country)
                .build();
    }

    static ComplaintResponse mapComplaintEntityToDto(final ComplaintEntity entity) {
        return ComplaintResponse.builder()
                .complaintId(entity.getId())
                .productId(entity.getProductId())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .reporterName(entity.getReporterName())
                .reporterEmail(entity.getReporterEmail())
                .reporterCountry(entity.getReporterCountry())
                .reportCount(entity.getReportCount())
                .build();
    }
}
