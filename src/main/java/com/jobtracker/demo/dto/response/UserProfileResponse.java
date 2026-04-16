package com.jobtracker.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String skills;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
