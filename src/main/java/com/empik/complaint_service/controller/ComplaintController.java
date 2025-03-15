package com.empik.complaint_service.controller;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.ComplaintResponse;
import com.empik.complaint_service.controller.model.UpdateComplaintRequest;
import com.empik.complaint_service.service.ComplaintService;
import com.empik.complaint_service.service.IpAddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
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
            @Valid @RequestBody final UpdateComplaintRequest request
            ) {
        return ResponseEntity.ok(complaintService.updateComplaint(id, request));
    }
}
