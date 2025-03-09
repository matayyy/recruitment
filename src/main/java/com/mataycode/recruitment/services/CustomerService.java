package com.mataycode.recruitment.services;

import com.mataycode.recruitment.domain.Customer;
import com.mataycode.recruitment.dto.CustomerDTO;
import com.mataycode.recruitment.dto.CustomerDTOMapper;
import com.mataycode.recruitment.dto.CustomerRegistrationRequest;
import com.mataycode.recruitment.exception.DuplicateResourceException;
import com.mataycode.recruitment.exception.InvalidEmailFormatException;
import com.mataycode.recruitment.exception.ResourceNotFoundException;
import com.mataycode.recruitment.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapper) {
        this.customerRepository = customerRepository;
        this.customerDTOMapper = customerDTOMapper;
    }

    public CustomerDTO registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {

        //validate email format (already checked at @CustomerRegistrationRequest)
//        validateEmailFormat(customerRegistrationRequest.email());

        //validate email uniqueness
        validateEmailUniqueness(customerRegistrationRequest.email());

        //create a new Customer object from registration request
        Customer customerToSave = extractCustomerRegistrationRequest(customerRegistrationRequest);

        //save the new customer
        customerRepository.save(customerToSave);

        //return customerDTO of saved customer
        return customerDTOMapper.apply(customerToSave);
    }

    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public CustomerDTO getCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerDTOMapper)
                .toList();
    }

    //Private methods -------------------------
    private void validateEmailFormat(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new InvalidEmailFormatException("Invalid email format: " + email);
        }
    }

    private void validateEmailUniqueness(String email) {
        if (customerRepository.findCustomerByEmail(email)
                .isPresent()) {
            throw new DuplicateResourceException("Email already taken");
        }
    }

    private Customer extractCustomerRegistrationRequest(CustomerRegistrationRequest request) {
        return new Customer(
                request.name(),
                request.email(),
                request.gender(),
                request.password(),
                request.birthDate()
        );
    }
}
