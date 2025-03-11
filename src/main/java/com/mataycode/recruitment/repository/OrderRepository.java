package com.mataycode.recruitment.repository;

import com.mataycode.recruitment.domain.Customer;
import com.mataycode.recruitment.domain.Status;
import com.mataycode.recruitment.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    Page<Order> findOrdersByStatus(Status status, Pageable pageable);

    Page<Order> findOrdersByCustomer_IdAndStatus(Long customerId, Status status, Pageable pageable);


    Page<Order> findOrdersByCustomer_Id(Long customerId, Pageable pageable);
}
