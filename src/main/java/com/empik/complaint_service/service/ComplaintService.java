package com.empik.complaint_service.service;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.ComplaintResponse;
import com.empik.complaint_service.controller.model.UpdateComplaintRequest;
import com.empik.complaint_service.exceptions.ComplaintNotFoundException;
import com.empik.complaint_service.repository.ComplaintEntity;
import com.empik.complaint_service.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final GeoLocationService geoLocationService;

    @Transactional
    public ComplaintResponse registerComplaint(final ComplaintRequest request, final String reporterIpAddress) {
        return complaintRepository.findByReporterNameAndProductId(request.reportedBy().name(), request.productId())
                .map(this::increaseComplaintReportCount)
                .orElseGet(() -> createNewComplaint(request, reporterIpAddress));
    }

    @Transactional
    public ComplaintResponse updateComplaint(final Long id, final UpdateComplaintRequest request) {
        return complaintRepository.findById(id)
                .map(entity -> updateAndSaveComplaint(entity, request.description()))
                .map(ComplaintMapper::mapComplaintEntityToDto)
                .orElseThrow(() -> handleComplaintNotFound(id));
    }

    public Page<ComplaintResponse> getComplaints(final PageRequest pageRequest) {
        log.info("Page request: {} {}", pageRequest.getPageSize(), pageRequest.getPageNumber());
        return complaintRepository.findAll(pageRequest)
                .map(ComplaintMapper::mapComplaintEntityToDto);
    }

    public ComplaintResponse getComplaint(final Long id) {
        return complaintRepository.findById(id)
                .map(ComplaintMapper::mapComplaintEntityToDto)
                .orElseThrow(() -> handleComplaintNotFound(id));
    }

    private ComplaintResponse increaseComplaintReportCount(final ComplaintEntity existingComplaint) {
        return Optional.of(existingComplaint)
                .map(complaint -> complaint.toBuilder()
                        .reportCount(complaint.getReportCount() + 1)
                        .build())
                .map(complaintRepository::save)
                .map(savedComplaint -> {
                    log.info("Complaint with id {} updated (report count incremented)", savedComplaint.getId());
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
                    log.info("Complaint with id {} successfully created", savedComplaint.getId());
                    return ComplaintMapper.mapComplaintEntityToDto(savedComplaint);
                })
                .orElseThrow();
    }

    private ComplaintEntity updateAndSaveComplaint(final ComplaintEntity entity, final String description) {
        log.info("Updating complaint with id: {}", entity.getId());
        entity.setDescription(description);
        return complaintRepository.save(entity);
    }

    private ComplaintNotFoundException handleComplaintNotFound(final Long id) {
        log.info("Complaint with id {} not found", id);
        return new ComplaintNotFoundException("Complaint with id " + id + " not found");
    }
}
