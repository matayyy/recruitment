package com.mataycode.recruitment.dto;

import com.mataycode.recruitment.domain.Gender;
import com.mataycode.recruitment.domain.Role;

import java.time.LocalDate;
import java.util.List;

public record CustomerDTO(
        Long id,
        String name,
        String email,
        Gender gender,
        LocalDate birthDate,
        List<Role> roles,
        String profileImageId) {
}