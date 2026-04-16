package com.jobtracker.demo.service;

import com.jobtracker.demo.dto.request.ChangePasswordRequest;
import com.jobtracker.demo.dto.request.UpdateProfileRequest;
import com.jobtracker.demo.dto.response.DashboardResponse;
import com.jobtracker.demo.dto.response.UserProfileResponse;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.entity.enums.ApplicationStatus;
import com.jobtracker.demo.exception.BadRequestException;
import com.jobtracker.demo.repository.ApplicationRepository;
import com.jobtracker.demo.repository.BookmarkRepository;
import com.jobtracker.demo.repository.JobRepository;
import com.jobtracker.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BookmarkRepository bookmarkRepository,
                       ApplicationRepository applicationRepository,
                       JobRepository jobRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfileResponse getProfile(User user) {
        return toProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(User user, UpdateProfileRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getSkills() != null) {
            user.setSkills(request.getSkills());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        User updatedUser = userRepository.save(user);
        logger.info("Profile updated for user: {}", user.getEmail());
        return toProfileResponse(updatedUser);
    }

    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        logger.info("Password changed for user: {}", user.getEmail());
    }

    public DashboardResponse getDashboard(User user) {
        Long userId = user.getId();

        long totalBookmarks = bookmarkRepository.countByUserId(userId);
        long totalApplications = applicationRepository.countByUserId(userId);
        long activeJobs = jobRepository.countByIsActiveTrue();

        Map<String, Long> appsByStatus = new HashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            long count = applicationRepository.countByUserIdAndStatus(userId, status);
            if (count > 0) {
                appsByStatus.put(status.name(), count);
            }
        }

        return DashboardResponse.builder()
                .totalBookmarks(totalBookmarks)
                .totalApplications(totalApplications)
                .applicationsByStatus(appsByStatus)
                .totalActiveJobs(activeJobs)
                .profile(toProfileResponse(user))
                .build();
    }

    private UserProfileResponse toProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .phone(user.getPhone())
                .skills(user.getSkills())
                .bio(user.getBio())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
