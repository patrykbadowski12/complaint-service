package com.empik.complaint_service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.empik.complaint_service.util.ComplaintTestFixtures.HEADER_NAME_X_FORWARDER_FOR;
import static com.empik.complaint_service.util.ComplaintTestFixtures.TEST_IP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IpAddressServiceTest {

    private final IpAddressService ipAddressService = new IpAddressService();
    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    void getClientIpWithXForwardedForHeader() {
        // given
        when(httpServletRequest.getHeader(HEADER_NAME_X_FORWARDER_FOR)).thenReturn(TEST_IP);

        // when
        final var result = ipAddressService.getClientIp(httpServletRequest);

        // then
        assertThat(result).isEqualTo(TEST_IP);
    }

    @Test
    void getClientIpFromRemoteAddress() {
        // given
        when(httpServletRequest.getHeader(HEADER_NAME_X_FORWARDER_FOR)).thenReturn(null);
        when(httpServletRequest.getRemoteAddr()).thenReturn(TEST_IP);

        // when
        final var result = ipAddressService.getClientIp(httpServletRequest);

        // then
        assertThat(result).isEqualTo(TEST_IP);
    }
}