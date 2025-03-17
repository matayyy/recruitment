package com.mataycode.recruitment.services;

import com.mataycode.recruitment.domain.ConfirmationToken;
import com.mataycode.recruitment.domain.Customer;
import com.mataycode.recruitment.dto.CustomerDTO;
import com.mataycode.recruitment.dto.CustomerDTOMapper;
import com.mataycode.recruitment.dto.CustomerRegistrationRequest;
import com.mataycode.recruitment.exception.DuplicateResourceException;
import com.mataycode.recruitment.exception.InvalidEmailFormatException;
import com.mataycode.recruitment.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;
    private final ConfirmationTokenService confirmationTokenService;
    private final CustomerService customerService;

    public RegistrationService(PasswordEncoder passwordEncoder, CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapper, ConfirmationTokenService confirmationTokenService, CustomerService customerService) {
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.customerDTOMapper = customerDTOMapper;
        this.confirmationTokenService = confirmationTokenService;
        this.customerService = customerService;
    }

    @Transactional
    public CustomerDTO registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {

        //validate email format (already checked at @CustomerRegistrationRequest)
//        validateEmailFormatOrThrow(customerRegistrationRequest.email());

        //validate email uniqueness
        validateEmailUniquenessOrThrow(customerRegistrationRequest.email());

        //create a new Customer object from registration request
        Customer customerToSave = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.gender(),
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.birthDate()
        );

        //ADD ROLE USER
//        customerToSave.setRoles(List.of(Role.USER)); -> moved to constructor.

        //save the new customer
        customerRepository.save(customerToSave);

        //TODO: CONSIDER MOVING THIS TO DIFFERENT METHOD.
        //createToken
        ConfirmationToken confirmationToken = new ConfirmationToken(
                UUID.randomUUID().toString(),
                customerToSave,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //send email to new customer //todo: add verification link to activate account
        try {
            new GMailer().sendMail(customerToSave.getEmail(),"Registration message.", """
                    Dear customer,

                    You are successfully registered with your email address.
                    To enable your account click link below.
                    %s

                    Best Regards,
                    Mataycode.
                    """.formatted("http://localhost:8080/api/v1/registration/confirm?token=" + confirmationToken.getToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //return customerDTO of saved customer
        return customerDTOMapper.apply(customerToSave);
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        customerService.enableCustomer(
                confirmationToken.getCustomer().getEmail());
        return "confirmed";
    }

    //PRIVATE METHODS

    private void validateEmailFormatOrThrow(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new InvalidEmailFormatException("Invalid email format: " + email);
        }
    }

    private void validateEmailUniquenessOrThrow(String email) {
        if (customerRepository.findCustomerByEmail(email)
                .isPresent()) {
            throw new DuplicateResourceException("Email already taken");
        }
    }
}
