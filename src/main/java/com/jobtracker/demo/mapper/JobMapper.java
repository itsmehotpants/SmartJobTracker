package com.jobtracker.demo.mapper;

import com.jobtracker.demo.dto.request.JobRequest;
import com.jobtracker.demo.dto.response.JobResponse;
import com.jobtracker.demo.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job toEntity(JobRequest request) {
        return Job.builder()
                .title(request.getTitle())
                .company(request.getCompany())
                .location(request.getLocation())
                .description(request.getDescription())
                .link(request.getLink())
                .salary(request.getSalary())
                .jobType(request.getJobType())
                .experienceLevel(request.getExperienceLevel())
                .deadline(request.getDeadline())
                .isActive(true)
                .build();
    }

    public JobResponse toResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .location(job.getLocation())
                .description(job.getDescription())
                .link(job.getLink())
                .salary(job.getSalary())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .deadline(job.getDeadline())
                .isActive(job.getIsActive())
                .createdBy(job.getCreatedBy())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    public void updateEntity(Job job, JobRequest request) {
        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setLink(request.getLink());
        job.setSalary(request.getSalary());
        job.setJobType(request.getJobType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setDeadline(request.getDeadline());
    }
}
