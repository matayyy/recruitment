package com.mataycode.recruitment.repository;

import com.mataycode.recruitment.domain.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {

    List<OrderStatusHistory> findOrderStatusHistoriesByOrder_Id(Long orderId);
}
