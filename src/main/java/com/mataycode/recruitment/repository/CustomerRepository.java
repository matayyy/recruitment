package com.mataycode.recruitment.repository;

import com.mataycode.recruitment.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findCustomerByEmail(String email);

    @Modifying
    @Query("UPDATE Customer c SET c.enabled = TRUE WHERE c.email = ?1")
    void enableCustomer(String email);
}