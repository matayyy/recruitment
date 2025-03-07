package com.mataycode.recruitment.repository;

import com.mataycode.recruitment.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
