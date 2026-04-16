package com.jobtracker.demo.controller;

import com.jobtracker.demo.dto.request.JobRequest;
import com.jobtracker.demo.dto.response.ApiResponse;
import com.jobtracker.demo.dto.response.JobResponse;
import com.jobtracker.demo.dto.response.JobStatsResponse;
import com.jobtracker.demo.dto.response.PagedResponse;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.entity.enums.ExperienceLevel;
import com.jobtracker.demo.entity.enums.JobType;
import com.jobtracker.demo.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jobs")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Jobs", description = "Job listing management — search, filter, CRUD (ADMIN)")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    @Operation(summary = "List all active jobs", description = "Returns paginated list of active jobs with sorting")
    public ResponseEntity<ApiResponse<PagedResponse<JobResponse>>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponse<JobResponse> response = jobService.getAllActiveJobs(pageable);
        return ResponseEntity.ok(ApiResponse.success("Jobs retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long id) {
        JobResponse response = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Job retrieved successfully", response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search jobs by keyword", description = "Searches in title, company, location, and description")
    public ResponseEntity<ApiResponse<PagedResponse<JobResponse>>> searchJobs(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<JobResponse> response = jobService.searchJobs(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved", response));
    }

    @GetMapping("/filter")
    @Operation(summary = "Advanced job search with filters", description = "Filter by keyword, job type, experience level, and location")
    public ResponseEntity<ApiResponse<PagedResponse<JobResponse>>> advancedSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) ExperienceLevel experienceLevel,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        PagedResponse<JobResponse> response = jobService.advancedSearch(keyword, jobType, experienceLevel, location, pageable);
        return ResponseEntity.ok(ApiResponse.success("Filtered results retrieved", response));
    }

    @GetMapping("/stats")
    @Operation(summary = "Get job statistics")
    public ResponseEntity<ApiResponse<JobStatsResponse>> getJobStats() {
        JobStatsResponse response = jobService.getJobStats();
        return ResponseEntity.ok(ApiResponse.success("Job statistics retrieved", response));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new job (ADMIN only)")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @Valid @RequestBody JobRequest request,
            @AuthenticationPrincipal User user) {

        JobResponse response = jobService.createJob(request, user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job created successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a job (ADMIN only)")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {

        JobResponse response = jobService.updateJob(id, request);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a job (ADMIN only)")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully"));
    }
}
