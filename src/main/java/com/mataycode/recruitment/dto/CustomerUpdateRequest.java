package com.mataycode.recruitment.dto;

import com.mataycode.recruitment.domain.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CustomerUpdateRequest(
        String name,
        @Email(message = "Invalid email format")
        String email,
        @PastOrPresent(message = "Date should be in the past")
        LocalDate birthDate,
        Gender gender,
        String profileImageId
) {
}
