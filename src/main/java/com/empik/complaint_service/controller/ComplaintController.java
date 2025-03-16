package com.empik.complaint_service.controller;

import com.empik.complaint_service.controller.dto.ComplaintRequest;
import com.empik.complaint_service.controller.dto.ComplaintResponse;
import com.empik.complaint_service.controller.dto.UpdateComplaintRequest;
import com.empik.complaint_service.service.ComplaintService;
import com.empik.complaint_service.service.geo.IpAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Operation(summary = "Register a new complaint", description = "Creates a new complaint and saves it in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complaint successfully created",
                    content = @Content(schema = @Schema(implementation = ComplaintResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<ComplaintResponse> registerComplaint(
            final HttpServletRequest httpServletRequest,
            @Valid @RequestBody final ComplaintRequest request) {
        return ResponseEntity.ok(complaintService.registerComplaint(request, ipAddressService.getClientIp(httpServletRequest)));
    }

    @Operation(summary = "Update an existing complaint", description = "Modifies the description of an existing complaint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complaint description successfully updated"),
            @ApiResponse(responseCode = "404", description = "Complaint with the given ID not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ComplaintResponse> updateComplaint(
            @PathVariable final Long id,
            @Valid @RequestBody final UpdateComplaintRequest request) {
        return ResponseEntity.ok(complaintService.updateComplaint(id, request));
    }

    @Operation(summary = "Get a paginated list of complaints", description = "Returns a paginated list of complaints.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of complaints"),
            @ApiResponse(responseCode = "204", description = "No complaints found")
    })
    @GetMapping
    public ResponseEntity<Page<ComplaintResponse>> getPagedComplaints(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Pattern(regexp = "asc|desc", message = "Sort direction must be 'asc' or 'desc'")
            @RequestParam(defaultValue = "desc") final String direction) {
        final var sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final var complaints = complaintService.getComplaints(PageRequest.of(page, size, sort));
        return complaints.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(complaints);
    }

    @Operation(summary = "Get a complaint by ID", description = "Returns details of a complaint based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Complaint found"),
            @ApiResponse(responseCode = "404", description = "Complaint with the given ID not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaint(@PathVariable final Long id) {
        return ResponseEntity.ok(complaintService.getComplaint(id));
    }
}
