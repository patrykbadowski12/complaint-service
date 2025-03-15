package com.empik.complaint_service.service.geo;

public record GeoIpResponse(String country,
                            String countryCode,
                            String city
) {
}
