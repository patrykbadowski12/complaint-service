package com.empik.complaint_service.service;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.ComplaintResponse;
import com.empik.complaint_service.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final GeoLocationService geoLocationService;

    public ComplaintResponse createComplaint(final ComplaintRequest request, final String reporterIpAddress) {
        final var reporterCountryByIp = geoLocationService.getCountryByIp(reporterIpAddress);
        return Optional.of(request)
                .map(req -> ComplaintMapper.createComplaintEntity(req, reporterCountryByIp))
                .map(complaintRepository::save)
                .map(savedComplaint -> {
                    log.debug("Complaint with id {} successfully created.", savedComplaint.getId());
                    return ComplaintMapper.mapComplaintEntityToDto(savedComplaint);
                })
                .orElseThrow();
    }
}
