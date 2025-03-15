package com.empik.complaint_service.service.mapper;

import com.empik.complaint_service.controller.dto.ComplaintResponse;
import com.empik.complaint_service.repository.ComplaintEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComplaintMapper {

    public static ComplaintResponse mapComplaintEntityToDto(final ComplaintEntity entity) {
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
