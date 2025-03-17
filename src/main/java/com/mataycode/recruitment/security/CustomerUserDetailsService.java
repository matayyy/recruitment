package com.mataycode.recruitment.security;

import com.mataycode.recruitment.domain.Customer;
import com.mataycode.recruitment.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findCustomerByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer: " + email + " not found"));
        return new CustomerUserDetails(customer);
    }
}