package com.empik.complaint_service.controller;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.ComplaintResponse;
import com.empik.complaint_service.controller.model.UpdateComplaintRequest;
import com.empik.complaint_service.service.ComplaintService;
import com.empik.complaint_service.service.IpAddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
@Validated
public class ComplaintController {

    private final ComplaintService complaintService;
    private final IpAddressService ipAddressService;

    @PostMapping
    public ResponseEntity<ComplaintResponse> registerComplaint(
            final HttpServletRequest httpServletRequest,
            @Valid @RequestBody final ComplaintRequest request) {
        return ResponseEntity.ok(complaintService.registerComplaint(request, ipAddressService.getClientIp(httpServletRequest)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintResponse> updateComplaint(
            @PathVariable final Long id,
            @Valid @RequestBody final UpdateComplaintRequest request) {
        return ResponseEntity.ok(complaintService.updateComplaint(id, request));
    }

    @GetMapping
    public ResponseEntity<Page<ComplaintResponse>> getPagedComplaints(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        final var complaints = complaintService.getComplaints(PageRequest.of(page, size));
        return complaints.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(complaints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaint(@PathVariable final Long id) {
        return ResponseEntity.ok(complaintService.getComplaint(id));
    }

}
