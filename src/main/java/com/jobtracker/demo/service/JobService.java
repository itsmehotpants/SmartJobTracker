package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.request.JobRequest;
import com.jobtracker.demo.dto.response.JobResponse;
import com.jobtracker.demo.dto.response.JobStatsResponse;
import com.jobtracker.demo.dto.response.PagedResponse;
import com.jobtracker.demo.entity.Job;
import com.jobtracker.demo.entity.enums.ExperienceLevel;
import com.jobtracker.demo.entity.enums.JobType;
import com.jobtracker.demo.exception.ResourceNotFoundException;
import com.jobtracker.demo.mapper.JobMapper;
import com.jobtracker.demo.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public JobService(JobRepository jobRepository, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
    }

    public PagedResponse<JobResponse> getAllActiveJobs(Pageable pageable) {
        Page<Job> jobPage = jobRepository.findByIsActiveTrue(pageable);
        return toPagedResponse(jobPage);
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return jobMapper.toResponse(job);
    }

    public PagedResponse<JobResponse> searchJobs(String keyword, Pageable pageable) {
        Page<Job> jobPage = jobRepository.searchByKeyword(keyword, pageable);
        return toPagedResponse(jobPage);
    }

    public PagedResponse<JobResponse> advancedSearch(String keyword, JobType jobType,
                                                      ExperienceLevel experienceLevel,
                                                      String location, Pageable pageable) {
        Page<Job> jobPage = jobRepository.advancedSearch(keyword, jobType, experienceLevel, location, pageable);
        return toPagedResponse(jobPage);
    }

    @Transactional
    public JobResponse createJob(JobRequest request, String createdBy) {
        Job job = jobMapper.toEntity(request);
        job.setCreatedBy(createdBy);
        Job savedJob = jobRepository.save(job);
        logger.info("Job created: {} by {}", savedJob.getTitle(), createdBy);
        return jobMapper.toResponse(savedJob);
    }

    @Transactional
    public JobResponse updateJob(Long id, JobRequest request) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        jobMapper.updateEntity(job, request);
        Job updatedJob = jobRepository.save(job);
        logger.info("Job updated: {}", updatedJob.getTitle());
        return jobMapper.toResponse(updatedJob);
    }

    @Transactional
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        jobRepository.delete(job);
        logger.info("Job deleted: {}", job.getTitle());
    }

    public JobStatsResponse getJobStats() {
        long totalJobs = jobRepository.count();
        long activeJobs = jobRepository.countByIsActiveTrue();

        Map<String, Long> jobsByType = new HashMap<>();
        for (JobType type : JobType.values()) {
            jobsByType.put(type.name(), jobRepository.countByJobType(type));
        }

        Map<String, Long> jobsByExperience = new HashMap<>();
        for (ExperienceLevel level : ExperienceLevel.values()) {
            jobsByExperience.put(level.name(), jobRepository.countByExperienceLevel(level));
        }

        return JobStatsResponse.builder()
                .totalJobs(totalJobs)
                .activeJobs(activeJobs)
                .expiredJobs(totalJobs - activeJobs)
                .jobsByType(jobsByType)
                .jobsByExperience(jobsByExperience)
                .build();
    }

    private PagedResponse<JobResponse> toPagedResponse(Page<Job> jobPage) {
        return PagedResponse.<JobResponse>builder()
                .content(jobPage.getContent().stream().map(jobMapper::toResponse).toList())
                .page(jobPage.getNumber())
                .size(jobPage.getSize())
                .totalElements(jobPage.getTotalElements())
                .totalPages(jobPage.getTotalPages())
                .last(jobPage.isLast())
                .build();
    }
}
