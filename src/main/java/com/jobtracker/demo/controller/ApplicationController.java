package com.jobtracker.demo.controller;

import com.jobtracker.demo.dto.request.ApplicationRequest;
import com.jobtracker.demo.dto.request.StatusUpdateRequest;
import com.jobtracker.demo.dto.response.ApiResponse;
import com.jobtracker.demo.dto.response.ApplicationResponse;
import com.jobtracker.demo.dto.response.ApplicationStatsResponse;
import com.jobtracker.demo.dto.response.AutoApplyResponse;
import com.jobtracker.demo.dto.response.PagedResponse;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.entity.enums.ApplicationStatus;
import com.jobtracker.demo.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/applications")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Applications", description = "Track job applications through the pipeline (Wishlist → Applied → Interview → Offered)")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @Operation(summary = "Apply to a job", description = "Creates a new application with WISHLIST status")
    public ResponseEntity<ApiResponse<ApplicationResponse>> createApplication(
            @Valid @RequestBody ApplicationRequest request,
            @AuthenticationPrincipal User user) {

        ApplicationResponse response = applicationService.createApplication(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application created successfully", response));
    }

    @PostMapping("/auto-apply")
    @Operation(summary = "Auto-Apply with AI Resumes Scan", description = "Analyzes passed Resume against Job Desc, returns score, and applies directly")
    public ResponseEntity<ApiResponse<AutoApplyResponse>> autoApply(
            @Valid @RequestBody ApplicationRequest request,
            @AuthenticationPrincipal User user) {

        AutoApplyResponse response = applicationService.autoApply(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("AI Scan complete and Application submitted!", response));
    }

    @GetMapping
    @Operation(summary = "Get my applications", description = "Returns paginated list with optional status filter")
    public ResponseEntity<ApiResponse<PagedResponse<ApplicationResponse>>> getMyApplications(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastUpdated").descending());
        PagedResponse<ApplicationResponse> response = applicationService.getUserApplications(user.getId(), status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application details")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getApplication(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        ApplicationResponse response = applicationService.getApplicationById(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Application retrieved successfully", response));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update application status", description = "Move application through the pipeline with validated transitions")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request,
            @AuthenticationPrincipal User user) {

        ApplicationResponse response = applicationService.updateStatus(id, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Application status updated successfully", response));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get application statistics", description = "Returns counts by status, interview rate, and offer rate")
    public ResponseEntity<ApiResponse<ApplicationStatsResponse>> getStats(@AuthenticationPrincipal User user) {
        ApplicationStatsResponse response = applicationService.getApplicationStats(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Application statistics retrieved", response));
    }

    @GetMapping("/export")
    @Operation(summary = "Export applications as CSV")
    public ResponseEntity<byte[]> exportApplicationsCsv(@AuthenticationPrincipal User user) {
        String csv = applicationService.exportApplicationsCsv(user.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "applications_" + user.getId() + ".csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv.getBytes());
    }
}
