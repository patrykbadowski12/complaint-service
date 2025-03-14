package com.empik.complaint_service.service;

import feign.FeignException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeoLocationService {

    private static final String UNKNOWN_VALUE = "Unknown";

    private final GeoIpFeignClient geoIpFeignClient;

    public String getCountryByIp(final String ip) {
        log.debug("Sending request to geoApiInfo with IP: {}", ip);

        try {
            final var response = geoIpFeignClient.getGeoIpInfo(ip);
            log.debug("Response from geoApiInfo: {}", response);
            return StringUtils.isNotEmpty(response.country()) ? response.country() : UNKNOWN_VALUE;
        } catch (FeignException e) {
            log.error("Error while fetching geo info for IP: {}. Error: {}", ip, e.getMessage());
            return UNKNOWN_VALUE;
        }
    }
}
