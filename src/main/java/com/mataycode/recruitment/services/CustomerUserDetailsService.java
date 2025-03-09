package com.mataycode.recruitment.services;

import com.mataycode.recruitment.domain.Customer;
import com.mataycode.recruitment.config.security.CustomerUserDetails;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findCustomerByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer " + username + " not found"));
        return new CustomerUserDetails(customer);
    }
}