package com.empik.complaint_service.util;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.Reporter;
import com.empik.complaint_service.controller.model.UpdateComplaintRequest;
import com.empik.complaint_service.repository.ComplaintEntity;
import com.empik.complaint_service.service.GeoIpResponse;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

public class ComplaintTestFixtures {

    public static final Long COMPLAINT_ID = 1L;
    public static final Long PRODUCT_ID = 8520L;
    public static final String DESCRIPTION = "Product is not working";
    public static final String UPDATED_DESCRIPTION = "Product is not working. Some parts are missing.";
    public static final String REPORTER_NAME = "Jan Kowalski";
    public static final String REPORTER_EMAIL = "jankowalski1995@email.com";
    public static final String COUNTRY = "Poland";
    public static final String COUNTRY_CODE = "PL";
    public static final String CITY = "Warsaw";
    public static final int REPORT_COUNT_ONE = 1;
    public static final int REPORT_COUNT_TWO = 2;
    public static final String TEST_IP = "77.65.32.1";
    public static final String HEADER_NAME_X_FORWARDER_FOR = "X-Forwarded-For";
    public static final String PAGE_ERROR_KEY = "getPagedComplaints.page";
    public static final String SIZE_ERROR_KEY = "getPagedComplaints.size";
    public static final int NUMBER_OF_COMPLAINTS = 1;

    public static ComplaintRequest createComplaintRequest() {
        return new ComplaintRequest(
                DESCRIPTION,
                new Reporter(REPORTER_NAME, REPORTER_EMAIL),
                PRODUCT_ID
        );
    }

    public static ComplaintEntity createComplaintEntity() {
        return createComplaintEntity(COMPLAINT_ID);
    }

    public static ComplaintEntity createComplaintEntityWithoutId() {
        return createComplaintEntity(null);
    }

    private static ComplaintEntity createComplaintEntity(Long id) {
        return ComplaintEntity.builder()
                .id(id)
                .productId(PRODUCT_ID)
                .description(DESCRIPTION)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .reporterName(REPORTER_NAME)
                .reporterEmail(REPORTER_EMAIL)
                .reporterCountry(COUNTRY)
                .reportCount(REPORT_COUNT_ONE)
                .build();
    }

    public static GeoIpResponse createGeoIpResponse() {
        return new GeoIpResponse(COUNTRY, COUNTRY_CODE, CITY);
    }

    public static UpdateComplaintRequest createUpdateComplaintRequest() {
        return new UpdateComplaintRequest(UPDATED_DESCRIPTION);
    }

    public static PageRequest createPageRequest() {
        return PageRequest.of(0, 20);
    }
}
