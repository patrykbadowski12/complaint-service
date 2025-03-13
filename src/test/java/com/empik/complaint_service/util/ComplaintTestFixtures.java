package com.empik.complaint_service.util;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.Reporter;
import com.empik.complaint_service.repository.ComplaintEntity;

import java.time.LocalDateTime;

public class ComplaintTestFixtures {

    public static final Long COMPLAINT_ID = 1L;
    public static final Long PRODUCT_ID = 8520L;
    public static final String DESCRIPTION = "Product is not working";
    public static final String REPORTER_NAME = "Jan Kowalski";
    public static final String REPORTER_EMAIL = "jankowalski1995@email.com";
    public static final String REPORTER_COUNTRY = "Poland";
    public static final int REPORT_COUNT = 1;

    public static ComplaintRequest createComplaintRequest() {
        return new ComplaintRequest(
                DESCRIPTION,
                new Reporter(REPORTER_NAME, REPORTER_EMAIL),
                PRODUCT_ID
        );
    }

    public static ComplaintEntity createComplaintEntity() {
        return ComplaintEntity.builder()
                .id(COMPLAINT_ID)
                .productId(PRODUCT_ID)
                .description(DESCRIPTION)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .reporterName(REPORTER_NAME)
                .reporterEmail(REPORTER_EMAIL)
                .reporterCountry(REPORTER_COUNTRY)
                .reportCount(REPORT_COUNT)
                .build();
    }
}
