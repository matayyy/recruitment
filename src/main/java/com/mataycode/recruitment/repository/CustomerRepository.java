package com.mataycode.recruitment.repository;

import com.mataycode.recruitment.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
