package com.mataycode.recruitment.repository;

import com.mataycode.recruitment.domain.Status;
import com.mataycode.recruitment.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOrderByStatus(Status status);

    Page<Order> findOrdersByStatus(Status status, Pageable pageable);
}
