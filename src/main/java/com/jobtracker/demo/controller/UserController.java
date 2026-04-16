package com.jobtracker.demo.controller;

import com.jobtracker.demo.dto.request.ChangePasswordRequest;
import com.jobtracker.demo.dto.request.UpdateProfileRequest;
import com.jobtracker.demo.dto.response.ApiResponse;
import com.jobtracker.demo.dto.response.DashboardResponse;
import com.jobtracker.demo.dto.response.UserProfileResponse;
import com.jobtracker.demo.entity.User;
import com.jobtracker.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "User profile management and dashboard")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(@AuthenticationPrincipal User user) {
        UserProfileResponse response = userService.getProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", response));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequest request) {

        UserProfileResponse response = userService.updateProfile(user, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    @PatchMapping("/me/password")
    @Operation(summary = "Change password", description = "Requires current password verification")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(user, request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get user dashboard", description = "Aggregated stats: bookmarks, applications, active jobs")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(@AuthenticationPrincipal User user) {
        DashboardResponse response = userService.getDashboard(user);
        return ResponseEntity.ok(ApiResponse.success("Dashboard retrieved successfully", response));
    }
}
