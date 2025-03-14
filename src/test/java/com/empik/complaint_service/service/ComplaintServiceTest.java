package com.empik.complaint_service.service;

import com.empik.complaint_service.repository.ComplaintEntity;
import com.empik.complaint_service.repository.ComplaintRepository;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.empik.complaint_service.util.ComplaintTestFixtures.COMPLAINT_ID;
import static com.empik.complaint_service.util.ComplaintTestFixtures.DESCRIPTION;
import static com.empik.complaint_service.util.ComplaintTestFixtures.PRODUCT_ID;
import static com.empik.complaint_service.util.ComplaintTestFixtures.COUNTRY;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_EMAIL;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_NAME;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORT_COUNT;
import static com.empik.complaint_service.util.ComplaintTestFixtures.TEST_IP;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createComplaintEntity;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createComplaintRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;
    @Mock
    private GeoLocationService geoLocationService;
    @InjectMocks
    private ComplaintService complaintService;

    @Test
    void createComplaint(final SoftAssertions softAssertions) {
        // given
        when(complaintRepository.save(any(ComplaintEntity.class))).thenReturn(createComplaintEntity());
        when(geoLocationService.getCountryByIp(TEST_IP)).thenReturn(COUNTRY);

        // when
        final var result = complaintService.createComplaint(createComplaintRequest(), TEST_IP);

        // then
        softAssertions.assertThat(result).satisfies(response -> {
            softAssertions.assertThat(response.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(response.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(response.description()).isEqualTo(DESCRIPTION);
            softAssertions.assertThat(response.createdAt()).isNotNull();
            softAssertions.assertThat(response.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(response.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(response.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(response.reportCount()).isEqualTo(REPORT_COUNT);
        });
    }
}