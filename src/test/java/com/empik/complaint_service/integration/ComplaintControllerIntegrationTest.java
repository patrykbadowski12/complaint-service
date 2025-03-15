package com.empik.complaint_service.integration;

import com.empik.complaint_service.controller.dto.ComplaintResponse;
import com.empik.complaint_service.repository.ComplaintRepository;
import com.empik.complaint_service.util.RestPageImpl;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static com.empik.complaint_service.util.ComplaintTestFixtures.COMPLAINT_ID;
import static com.empik.complaint_service.util.ComplaintTestFixtures.COUNTRY;
import static com.empik.complaint_service.util.ComplaintTestFixtures.DESCRIPTION;
import static com.empik.complaint_service.util.ComplaintTestFixtures.NUMBER_OF_COMPLAINTS;
import static com.empik.complaint_service.util.ComplaintTestFixtures.PAGE_ERROR_KEY;
import static com.empik.complaint_service.util.ComplaintTestFixtures.PRODUCT_ID;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_EMAIL;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_NAME;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORT_COUNT_ONE;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORT_COUNT_TWO;
import static com.empik.complaint_service.util.ComplaintTestFixtures.SIZE_ERROR_KEY;
import static com.empik.complaint_service.util.ComplaintTestFixtures.TEST_IP;
import static com.empik.complaint_service.util.ComplaintTestFixtures.UPDATED_DESCRIPTION;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createComplaintEntityWithoutId;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createComplaintRequest;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createUpdateComplaintRequest;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ComplaintControllerIntegrationTest {

    private static final String DEFAULT_URI = "/api/v1/complaints";
    private static final String HEADER_NAME_X_FORWARDER_FOR = "X-Forwarded-For";

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ComplaintRepository complaintRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE COMPLAINTS RESTART IDENTITY CASCADE");
    }

    @Test
    void createNewComplaint(final SoftAssertions softAssertions) {
        // given
        final var headers = new HttpHeaders();
        headers.set(HEADER_NAME_X_FORWARDER_FOR, TEST_IP);
        headers.setContentType(MediaType.APPLICATION_JSON);
        final var request = new HttpEntity<>(createComplaintRequest(), headers);

        // when
        final var response = testRestTemplate.postForEntity(
                DEFAULT_URI,
                request,
                ComplaintResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        softAssertions.assertThat(response.getBody()).satisfies(complaintResponse -> {
            softAssertions.assertThat(complaintResponse.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(complaintResponse.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(complaintResponse.description()).isEqualTo(DESCRIPTION);
            softAssertions.assertThat(complaintResponse.createdAt()).isNotNull();
            softAssertions.assertThat(complaintResponse.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(complaintResponse.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(complaintResponse.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(complaintResponse.reportCount()).isEqualTo(REPORT_COUNT_ONE);
        });
    }

    @Test
    void increaseComplaintReportCount(final SoftAssertions softAssertions) {
        // given
        complaintRepository.save(createComplaintEntityWithoutId());
        final var headers = new HttpHeaders();
        headers.set(HEADER_NAME_X_FORWARDER_FOR, TEST_IP);
        headers.setContentType(MediaType.APPLICATION_JSON);
        final var request = new HttpEntity<>(createComplaintRequest(), headers);

        // when
        final var response = testRestTemplate.postForEntity(
                DEFAULT_URI,
                request,
                ComplaintResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        softAssertions.assertThat(response.getBody()).satisfies(complaintResponse -> {
            softAssertions.assertThat(complaintResponse.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(complaintResponse.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(complaintResponse.description()).isEqualTo(DESCRIPTION);
            softAssertions.assertThat(complaintResponse.createdAt()).isNotNull();
            softAssertions.assertThat(complaintResponse.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(complaintResponse.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(complaintResponse.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(complaintResponse.reportCount()).isEqualTo(REPORT_COUNT_TWO);
        });
    }

    @Test
    void updateComplaintDescription(final SoftAssertions softAssertions) {
        // given
        complaintRepository.save(createComplaintEntityWithoutId());

        // when
        final var response = testRestTemplate.exchange(
                DEFAULT_URI + "/" + COMPLAINT_ID,
                HttpMethod.PUT,
                new HttpEntity<>(createUpdateComplaintRequest()),
                ComplaintResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        softAssertions.assertThat(response.getBody()).satisfies(complaintResponse -> {
            softAssertions.assertThat(complaintResponse.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(complaintResponse.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(complaintResponse.description()).isEqualTo(UPDATED_DESCRIPTION);
            softAssertions.assertThat(complaintResponse.createdAt()).isNotNull();
            softAssertions.assertThat(complaintResponse.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(complaintResponse.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(complaintResponse.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(complaintResponse.reportCount()).isEqualTo(REPORT_COUNT_ONE);
        });
    }

    @Test
    void updateComplaintDescriptionReturnNotFound () {
        // given & when
        final var response = testRestTemplate.exchange(
                DEFAULT_URI + "/" + COMPLAINT_ID,
                HttpMethod.PUT,
                new HttpEntity<>(createUpdateComplaintRequest()),
                String.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Complaint with id " + COMPLAINT_ID + " not found");
    }

    @Test
    void getComplaintsList() {
        // given
        complaintRepository.save(createComplaintEntityWithoutId());

        // when
        final var response = testRestTemplate.exchange(
                DEFAULT_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<ComplaintResponse>>() {}
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(NUMBER_OF_COMPLAINTS);
    }

    @Test
    void getComplaintsWithNoContentStatus() {
        // given & when
        final var response = testRestTemplate.exchange(
                DEFAULT_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<RestPageImpl<ComplaintResponse>>() {}
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void getComplaintsWithBadRequestError() {
        // given & when
        final var response = testRestTemplate.exchange(
                DEFAULT_URI + "?size=200&page=-1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey(PAGE_ERROR_KEY);
        assertThat(response.getBody()).containsKey(SIZE_ERROR_KEY);
    }

    @Test
    void getComplaint(final SoftAssertions softAssertions) {
        // given
        complaintRepository.save(createComplaintEntityWithoutId());

        // when
        final var response = testRestTemplate.getForEntity(
                DEFAULT_URI + "/" + COMPLAINT_ID,
                ComplaintResponse.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        softAssertions.assertThat(response.getBody()).satisfies(complaintResponse -> {
            softAssertions.assertThat(complaintResponse.complaintId()).isEqualTo(COMPLAINT_ID);
            softAssertions.assertThat(complaintResponse.productId()).isEqualTo(PRODUCT_ID);
            softAssertions.assertThat(complaintResponse.description()).isEqualTo(DESCRIPTION);
            softAssertions.assertThat(complaintResponse.createdAt()).isNotNull();
            softAssertions.assertThat(complaintResponse.reporterName()).isEqualTo(REPORTER_NAME);
            softAssertions.assertThat(complaintResponse.reporterEmail()).isEqualTo(REPORTER_EMAIL);
            softAssertions.assertThat(complaintResponse.reporterCountry()).isEqualTo(COUNTRY);
            softAssertions.assertThat(complaintResponse.reportCount()).isEqualTo(REPORT_COUNT_ONE);
        });
    }

    @Test
    void getComplaintWithNotFoundError() {
        // given & when
        final var response = testRestTemplate.getForEntity(
                DEFAULT_URI + "/" + COMPLAINT_ID,
                String.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Complaint with id " + COMPLAINT_ID + " not found");
    }
}
