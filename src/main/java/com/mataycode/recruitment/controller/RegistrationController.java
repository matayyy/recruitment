package com.mataycode.recruitment.controller;

import com.mataycode.recruitment.dto.CustomerDTO;
import com.mataycode.recruitment.dto.CustomerRegistrationRequest;
import com.mataycode.recruitment.services.CustomerService;
import com.mataycode.recruitment.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping()
    public ResponseEntity<CustomerDTO> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest registrationRequest) {
        CustomerDTO customerDTO = registrationService.registerCustomer(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
