package com.empik.complaint_service.service;

public record GeoIpResponse(String country,
                            String countryCode,
                            String city
) {
}
