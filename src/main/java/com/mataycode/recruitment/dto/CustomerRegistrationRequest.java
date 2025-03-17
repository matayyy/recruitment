package com.mataycode.recruitment.dto;

import com.mataycode.recruitment.domain.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CustomerRegistrationRequest(

        //REMEMBER TO USE @VALID AT CONTROLLERS!
        String name,
        @Email(message = "Invalid email format")
        String email,
        String password,
        Gender gender,
        @PastOrPresent(message = "Date should be in the past")
        LocalDate birthDate
) {
}
