package com.empik.complaint_service.service.factory;

import com.empik.complaint_service.controller.dto.ComplaintRequest;
import com.empik.complaint_service.repository.ComplaintEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComplaintFactory {

    public static ComplaintEntity createComplaintEntity(final ComplaintRequest request, final String country) {
        return ComplaintEntity.builder()
                .productId(request.productId())
                .description(request.description())
                .reporterName(request.reportedBy().name())
                .reporterEmail(request.reportedBy().email())
                .reporterCountry(country)
                .build();
    }
}
