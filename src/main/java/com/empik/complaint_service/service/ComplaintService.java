package com.empik.complaint_service.service;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.ComplaintResponse;
import com.empik.complaint_service.repository.ComplaintEntity;
import com.empik.complaint_service.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final GeoLocationService geoLocationService;

    public ComplaintResponse registerComplaint(final ComplaintRequest request, final String reporterIpAddress) {
        return complaintRepository.findByReporterNameAndProductId(request.reportedBy().name(), request.productId())
                .map(this::updateExistingComplaint)
                .orElseGet(() -> createNewComplaint(request, reporterIpAddress));
    }

    @Transactional
    private ComplaintResponse updateExistingComplaint(final ComplaintEntity existingComplaint) {
        return Optional.of(existingComplaint)
                .map(complaint -> complaint.toBuilder()
                        .reportCount(complaint.getReportCount() + 1)
                        .build())
                .map(complaintRepository::save)
                .map(savedComplaint -> {
                    log.debug("Complaint with id {} updated (report count incremented).", savedComplaint.getId());
                    return ComplaintMapper.mapComplaintEntityToDto(savedComplaint);
                })
                .orElseThrow();
    }

    private ComplaintResponse createNewComplaint(final ComplaintRequest request, final String reporterIpAddress) {
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
