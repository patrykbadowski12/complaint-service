package com.empik.complaint_service.controller;

import com.empik.complaint_service.controller.model.ComplaintRequest;
import com.empik.complaint_service.controller.model.ComplaintResponse;
import com.empik.complaint_service.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(@Valid @RequestBody final ComplaintRequest request) {
        return ResponseEntity.ok(complaintService.createComplaint(request));
    }
}
