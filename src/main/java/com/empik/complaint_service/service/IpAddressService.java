package com.empik.complaint_service.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class IpAddressService {
    private static final String HEADER_NAME_X_FORWARDER_FOR = "X-Forwarded-For";
    public static final String UNKNOWN_VALUE = "unknown";

    public String getClientIp(final HttpServletRequest request) {
        var ip = request.getHeader(HEADER_NAME_X_FORWARDER_FOR);

        if (ip == null || ip.isEmpty() || UNKNOWN_VALUE.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
