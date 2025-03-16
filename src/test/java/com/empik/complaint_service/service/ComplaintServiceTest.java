package com.empik.complaint_service.service;

import com.empik.complaint_service.exceptions.ComplaintNotFoundException;
import com.empik.complaint_service.repository.ComplaintEntity;
import com.empik.complaint_service.repository.ComplaintRepository;
import com.empik.complaint_service.service.geo.GeoIPService;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static com.empik.complaint_service.util.ComplaintTestFixtures.COMPLAINT_ID;
import static com.empik.complaint_service.util.ComplaintTestFixtures.COUNTRY;
import static com.empik.complaint_service.util.ComplaintTestFixtures.DESCRIPTION;
import static com.empik.complaint_service.util.ComplaintTestFixtures.NUMBER_OF_COMPLAINTS_ONE;
import static com.empik.complaint_service.util.ComplaintTestFixtures.PRODUCT_ID;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_EMAIL;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_NAME;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORT_COUNT_ONE;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORT_COUNT_TWO;
import static com.empik.complaint_service.util.ComplaintTestFixtures.TEST_IP;
import static com.empik.complaint_service.util.ComplaintTestFixtures.UPDATED_DESCRIPTION;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createComplaintEntity;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createComplaintRequest;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createPageRequest;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createUpdateComplaintRequest;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
class ComplaintServiceTest {
    @Mock
    private ComplaintRepository complaintRepository;
    @Mock
    private GeoIPService geoIPService;
    @InjectMocks
    private ComplaintService complaintService;

    @Test
    void createNewComplaint(final SoftAssertions softAssertions) {
        // given
        when(complaintRepository.findByReporterNameAndProductId(anyString(), anyLong())).thenReturn(Optional.empty());
        when(complaintRepository.save(any(ComplaintEntity.class))).thenReturn(createComplaintEntity());
        when(geoIPService.getCountryByIp(TEST_IP)).thenReturn(COUNTRY);

        // when
        final var result = complaintService.registerComplaint(createComplaintRequest(), TEST_IP);

        // then
        softAssertions.assertThat(result).satisfies(response -> {
            softAssertions.assertThat(response.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(response.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(response.description()).isEqualTo(DESCRIPTION);
            softAssertions.assertThat(response.createdAt()).isNotNull();
            softAssertions.assertThat(response.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(response.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(response.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(response.reportCount()).isEqualTo(REPORT_COUNT_ONE);
        });
    }

    @Test
    void increaseReportCountForExistingComplaint(final SoftAssertions softAssertions) {
        // given
        final var complaintEntity = createComplaintEntity();
        when(complaintRepository.findByReporterNameAndProductId(anyString(), anyLong())).thenReturn(of(complaintEntity));
        complaintEntity.setReportCount(complaintEntity.getReportCount() + 1);
        when(complaintRepository.save(any(ComplaintEntity.class))).thenReturn(complaintEntity);

        // when
        final var result = complaintService.registerComplaint(createComplaintRequest(), TEST_IP);

        // then
        softAssertions.assertThat(result).satisfies(response -> {
            softAssertions.assertThat(response.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(response.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(response.description()).isEqualTo(DESCRIPTION);
            softAssertions.assertThat(response.createdAt()).isNotNull();
            softAssertions.assertThat(response.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(response.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(response.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(response.reportCount()).isEqualTo(REPORT_COUNT_TWO);
        });
    }

    @Test
    void updateDescriptionForExistingComplaint(final SoftAssertions softAssertions) {
        // given
        final var complaintEntity = createComplaintEntity();
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(of(complaintEntity));
        complaintEntity.setDescription(UPDATED_DESCRIPTION);
        when(complaintRepository.save(any(ComplaintEntity.class))).thenReturn(complaintEntity);
        // when
        final var result = complaintService.updateComplaint(COMPLAINT_ID, createUpdateComplaintRequest());

        // then
        softAssertions.assertThat(result).satisfies(response -> {
            softAssertions.assertThat(response.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(response.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(response.description()).isEqualTo(UPDATED_DESCRIPTION);
            softAssertions.assertThat(response.createdAt()).isNotNull();
            softAssertions.assertThat(response.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(response.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(response.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(response.reportCount()).isEqualTo(REPORT_COUNT_ONE);
        });
    }

    @Test
    void throwErrorDuringUpdateIfComplaintNotExist() {
        // given
        final var request = createUpdateComplaintRequest();
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> complaintService.updateComplaint(COMPLAINT_ID, request))
                .isInstanceOf(ComplaintNotFoundException.class)
                .hasMessage("Complaint with id " + COMPLAINT_ID + " not found");
    }

    @Test
    void getComplaintsList() {
        // given
        final var complaintsPage = new PageImpl<>(List.of(createComplaintEntity()), createPageRequest(), NUMBER_OF_COMPLAINTS_ONE);
        when(complaintRepository.findAll(createPageRequest())).thenReturn(complaintsPage);

        // when
        final var result = complaintService.getComplaints(createPageRequest());

        // then
        assertThat(result).isNotEmpty().hasSize(NUMBER_OF_COMPLAINTS_ONE);
    }

    @Test
    void getSingleComplaint(final SoftAssertions softAssertions) {
        // given
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(ofNullable(createComplaintEntity()));

        // when
        final var result = complaintService.getComplaint(COMPLAINT_ID);

        // then
        softAssertions.assertThat(result).satisfies(response -> {
            softAssertions.assertThat(response.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(response.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(response.description()).isEqualTo(DESCRIPTION);
            softAssertions.assertThat(response.createdAt()).isNotNull();
            softAssertions.assertThat(response.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(response.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(response.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(response.reportCount()).isEqualTo(REPORT_COUNT_ONE);
        });
    }

    @Test
    void getSingleComplaintNotFound() {
        assertThatThrownBy(() -> complaintService.getComplaint(COMPLAINT_ID))
                .isInstanceOf(ComplaintNotFoundException.class)
                .hasMessage("Complaint with id " + COMPLAINT_ID + " not found");
    }
}