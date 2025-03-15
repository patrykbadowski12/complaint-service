package com.empik.complaint_service.service;

import com.empik.complaint_service.service.geo.GeoIPService;
import com.empik.complaint_service.service.geo.GeoIpFeignClient;
import com.empik.complaint_service.service.geo.GeoIpResponse;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static com.empik.complaint_service.util.ComplaintTestFixtures.COUNTRY;
import static com.empik.complaint_service.util.ComplaintTestFixtures.TEST_IP;
import static com.empik.complaint_service.util.ComplaintTestFixtures.createGeoIpResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(MockitoExtension.class)
class GeoIPServiceTest {

    private static final String UNKNOWN_VALUE = "Unknown";
    private static final String TEST_URL = "http://test-url";
    private static final String ERROR_MESSAGE = "API error";

    @Mock
    private GeoIpFeignClient geoIpFeignClient;
    @InjectMocks
    private GeoIPService geoIPService;

    @Test
    void getCountryByIp() {
        // given
        when(geoIpFeignClient.getGeoIpInfo(TEST_IP)).thenReturn(createGeoIpResponse());

        // when
        final var result = geoIPService.getCountryByIp(TEST_IP);

        // then
        assertThat(result).isEqualTo(COUNTRY);
    }

    @Test
    void getCountryWithEmptyResponse() {
        // given
        when(geoIpFeignClient.getGeoIpInfo(TEST_IP)).thenReturn(new GeoIpResponse(null, null, null));

        // when
        final var result = geoIPService.getCountryByIp(TEST_IP);

        // then
        assertThat(result).isEqualTo(UNKNOWN_VALUE);
    }

    @Test
    void shouldReturnUnknownWhenGeoApiThrowsException() {
        // given
        when(geoIpFeignClient.getGeoIpInfo(TEST_IP)).thenThrow(FeignException.errorStatus(ERROR_MESSAGE, createApiResponseWithError()));

        // when
        final var country = geoIPService.getCountryByIp(TEST_IP);

        // then
        assertEquals(UNKNOWN_VALUE, country);
    }

    private Response createApiResponseWithError() {
        return Response.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .reason(INTERNAL_SERVER_ERROR.getReasonPhrase())
                .headers(Collections.emptyMap())
                .request(createRequest())
                .build();
    }

    private static Request createRequest() {
        return Request.create(Request.HttpMethod.GET, TEST_URL, Collections.emptyMap(), null, StandardCharsets.UTF_8, null);
    }
}