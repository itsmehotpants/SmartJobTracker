package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.request.ApplicationRequest;
import com.jobtracker.demo.dto.request.StatusUpdateRequest;
import com.jobtracker.demo.dto.response.ApplicationResponse;
import com.jobtracker.demo.dto.response.ApplicationStatsResponse;
import com.jobtracker.demo.dto.response.AutoApplyResponse;
import com.jobtracker.demo.dto.response.PagedResponse;
import com.jobtracker.demo.entity.Application;
import com.jobtracker.demo.entity.Job;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.entity.enums.ApplicationStatus;
import com.jobtracker.demo.exception.BadRequestException;
import com.jobtracker.demo.exception.DuplicateResourceException;
import com.jobtracker.demo.exception.ResourceNotFoundException;
import com.jobtracker.demo.mapper.ApplicationMapper;
import com.jobtracker.demo.repository.ApplicationRepository;
import com.jobtracker.demo.repository.JobRepository;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ApplicationMapper applicationMapper;

    public ApplicationService(ApplicationRepository applicationRepository,
                              JobRepository jobRepository,
                              ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.applicationMapper = applicationMapper;
    }

    @Transactional
    public ApplicationResponse createApplication(ApplicationRequest request, User user) {
        if (applicationRepository.existsByUserIdAndJobId(user.getId(), request.getJobId())) {
            throw new DuplicateResourceException("You have already applied to this job");
        }

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + request.getJobId()));

        Application application = Application.builder()
                .user(user)
                .job(job)
                .status(ApplicationStatus.WISHLIST)
                .notes(request.getNotes())
                .resumeLink(request.getResumeLink())
                .build();

        Application saved = applicationRepository.save(application);
        logger.info("Application created: user={} job={}", user.getEmail(), job.getTitle());
        return applicationMapper.toResponse(saved);
    }

    @Transactional
    public AutoApplyResponse autoApply(ApplicationRequest request, User user) {
        if (applicationRepository.existsByUserIdAndJobId(user.getId(), request.getJobId())) {
            throw new DuplicateResourceException("You have already applied to this job");
        }

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + request.getJobId()));

        String resumeText = request.getResumeText() != null ? request.getResumeText().toLowerCase() : "";
        String jobDesc = job.getDescription().toLowerCase();

        // Simulated AI Keyword Matching
        List<String> keyTechWords = Arrays.asList("java", "spring", "react", "python", "sql", "aws", "docker", "kubernetes", "cloud", "agile", "c++", "c#", "node", "javascript", "typescript", "data", "machine learning", "flutter", "mobile", "frontend", "backend", "full stack", "design", "ui", "ux");
        
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        
        for (String word : keyTechWords) {
            if (jobDesc.contains(word)) {
                if (resumeText.contains(word)) {
                    matched.add(word);
                } else {
                    missing.add(word);
                }
            }
        }

        int score = 50; // Base score
        if (!matched.isEmpty() || !missing.isEmpty()) {
            score = (int) (((double) matched.size() / (matched.size() + missing.size())) * 100);
            score = Math.max(15, Math.min(score, 100)); 
        } else if (resumeText.trim().length() > 50) {
            score = 65; // Decent resume but no specific buzzwords matched
        }

        String aiNotes = "AI Resume Scan via Smart Tracker:\nMatch Score: " + score + "%\n" +
                         "Matched Skills: " + String.join(", ", matched) + "\n" +
                         "Missing Skills: " + String.join(", ", missing);

        Application application = Application.builder()
                .user(user)
                .job(job)
                .status(ApplicationStatus.APPLIED) // Automatically applied
                .notes(request.getNotes() != null ? request.getNotes() + "\n\n" + aiNotes : aiNotes)
                .resumeLink(request.getResumeLink())
                .build();

        Application saved = applicationRepository.save(application);
        logger.info("Auto-Application created: user={} job={} score={}", user.getEmail(), job.getTitle(), score);

        return AutoApplyResponse.builder()
                .application(applicationMapper.toResponse(saved))
                .matchScore(score)
                .analysisSummary("Your resume is a " + score + "% match based on keyword analysis against the job description.")
                .matchedKeywords(matched)
                .missingKeywords(missing)
                .build();
    }

    public PagedResponse<ApplicationResponse> getUserApplications(Long userId, ApplicationStatus status, Pageable pageable) {
        Page<Application> page;
        if (status != null) {
            page = applicationRepository.findByUserIdAndStatus(userId, status, pageable);
        } else {
            page = applicationRepository.findByUserId(userId, pageable);
        }

        return PagedResponse.<ApplicationResponse>builder()
                .content(page.getContent().stream().map(applicationMapper::toResponse).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public ApplicationResponse getApplicationById(Long id, Long userId) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Application not found with id: " + id);
        }

        return applicationMapper.toResponse(application);
    }

    @Transactional
    public ApplicationResponse updateStatus(Long id, StatusUpdateRequest request, Long userId) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));

        if (!application.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Application not found with id: " + id);
        }

        // Validate status transition
        validateStatusTransition(application.getStatus(), request.getStatus());

        application.setStatus(request.getStatus());
        if (request.getNotes() != null) {
            application.setNotes(request.getNotes());
        }

        Application updated = applicationRepository.save(application);
        logger.info("Application status updated: id={} from={} to={}",
                id, application.getStatus(), request.getStatus());
        return applicationMapper.toResponse(updated);
    }

    public ApplicationStatsResponse getApplicationStats(Long userId) {
        long total = applicationRepository.countByUserId(userId);

        Map<String, Long> byStatus = new HashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            byStatus.put(status.name(), applicationRepository.countByUserIdAndStatus(userId, status));
        }

        long interviewCount = byStatus.getOrDefault("INTERVIEW", 0L);
        long offerCount = byStatus.getOrDefault("OFFERED", 0L) + byStatus.getOrDefault("ACCEPTED", 0L);

        return ApplicationStatsResponse.builder()
                .totalApplications(total)
                .applicationsByStatus(byStatus)
                .interviewRate(total > 0 ? (double) interviewCount / total * 100 : 0)
                .offerRate(total > 0 ? (double) offerCount / total * 100 : 0)
                .build();
    }

    public String exportApplicationsCsv(Long userId) {
        List<Application> applications = applicationRepository.findByUserId(userId);

        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            csvWriter.writeNext(new String[]{
                    "Application ID", "Job Title", "Company", "Location",
                    "Status", "Notes", "Resume Link", "Applied Date", "Last Updated"
            });

            for (Application app : applications) {
                Job job = app.getJob();
                csvWriter.writeNext(new String[]{
                        String.valueOf(app.getId()),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation(),
                        app.getStatus().name(),
                        app.getNotes() != null ? app.getNotes() : "",
                        app.getResumeLink() != null ? app.getResumeLink() : "N/A",
                        app.getAppliedDate().toString(),
                        app.getLastUpdated().toString()
                });
            }
        } catch (Exception e) {
            logger.error("Error exporting applications to CSV", e);
            throw new RuntimeException("Failed to export applications", e);
        }

        return stringWriter.toString();
    }

    private void validateStatusTransition(ApplicationStatus current, ApplicationStatus target) {
        // Prevent illogical transitions
        if (current == ApplicationStatus.ACCEPTED || current == ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Cannot change status from " + current + ". This application is finalized.");
        }
        if (current == ApplicationStatus.REJECTED && target != ApplicationStatus.WITHDRAWN) {
            throw new BadRequestException("Cannot change status from REJECTED to " + target +
                    ". You can only withdraw a rejected application.");
        }
    }
}
