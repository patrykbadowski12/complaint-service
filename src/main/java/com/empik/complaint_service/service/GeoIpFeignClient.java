package com.empik.complaint_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "geoApiClient", url = "http://ip-api.com")
public interface GeoIpFeignClient {

    @GetMapping("/json/{ip}")
    GeoIpResponse getGeoIpInfo(@PathVariable String ip);
}
