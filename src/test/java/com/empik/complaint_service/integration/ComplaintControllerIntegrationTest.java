package com.empik.complaint_service.integration;

import com.empik.complaint_service.controller.model.ComplaintResponse;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.empik.complaint_service.util.ComplaintTestFixtures.COMPLAINT_ID;
import static com.empik.complaint_service.util.ComplaintTestFixtures.DESCRIPTION;
import static com.empik.complaint_service.util.ComplaintTestFixtures.PRODUCT_ID;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_COUNTRY;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_EMAIL;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORTER_NAME;
import static com.empik.complaint_service.util.ComplaintTestFixtures.REPORT_COUNT;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createComplaintRequest;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ComplaintControllerIntegrationTest {

    private static final String DEFAULT_URI = "/api/v1/complaints";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE COMPLAINTS RESTART IDENTITY CASCADE");
    }
    @Test
    void shouldCreateComplaint(final SoftAssertions softAssertions) {
        // given & when
        final var response = testRestTemplate.postForEntity(
                DEFAULT_URI,
                new HttpEntity<>(createComplaintRequest()),
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
            softAssertions.assertThat(complaintResponse.reporterCountry()).isEqualTo(REPORTER_COUNTRY);
            softAssertions.assertThat(complaintResponse.reportCount()).isEqualTo(REPORT_COUNT);
        });
    }
}