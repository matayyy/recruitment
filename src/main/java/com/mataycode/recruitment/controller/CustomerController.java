package com.mataycode.recruitment.controller;

import com.mataycode.recruitment.dto.CustomerDTO;
import com.mataycode.recruitment.dto.CustomerRegistrationRequest;
import com.mataycode.recruitment.dto.CustomerUpdateRequest;
import com.mataycode.recruitment.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/me")
    public ResponseEntity<String> getMe(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }

    @PostMapping("/register")
    public ResponseEntity<CustomerDTO> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest registrationRequest) {
        CustomerDTO customerDTO = customerService.registerCustomer(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        return customerService.deleteCustomerById(customerId);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        CustomerDTO customerDTO = customerService.getCustomerById(customerId);

        return ResponseEntity.ok(customerDTO);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok().body(customerService.getAllCustomers());
    }

    @PutMapping("/{customerId}")
    public void updateCustomer(@PathVariable Long customerId, @Valid @RequestBody CustomerUpdateRequest updateRequest) {
        customerService.updateCustomer(customerId, updateRequest);
    }

    //todo: ADD LOGIC TO SAVE AND UPDATE CUSTOMER_PROFILE_IMAGE_ID
}
