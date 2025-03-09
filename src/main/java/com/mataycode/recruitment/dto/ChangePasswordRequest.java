package com.mataycode.recruitment.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "Password is required")
        String oldPassword,
        @NotBlank(message = "New password is required")
        String newPassword,
        @NotBlank(message = "Confirm new password is required")
        String confirmNewPassword
) {}
