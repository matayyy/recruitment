package com.mataycode.recruitment.dto;

import com.mataycode.recruitment.domain.Customer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerDTOMapper implements Function<Customer, CustomerDTO> {

    @Override
    public CustomerDTO apply(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getGender(),
                customer.getBirthDate(),
                customer.getRoles(),
                customer.getProfileImageId()
        );
    }
}
