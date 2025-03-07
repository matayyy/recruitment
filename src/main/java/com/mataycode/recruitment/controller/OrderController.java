package com.mataycode.recruitment.controller;

import com.mataycode.recruitment.domain.OrderStatusHistory;
import com.mataycode.recruitment.domain.Status;
import com.mataycode.recruitment.domain.Order;
import com.mataycode.recruitment.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody List<Long> productsIds) {
        Order order = orderService.createOrder(productsIds);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if(orderService.deleteOrderById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Can be filtered by status.
    @GetMapping
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) Status status) {

        Page<Order> orders;

        if (status != null) {
            orders = orderService.getOrdersByStatus(status, PageRequest.of(page, size));
        } else {
            orders = orderService.getOrders(PageRequest.of(page, size));
        }

        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam Status status) {
        Order updatedOrder = orderService.updateStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<OrderStatusHistory>> getOrderStatusHistory(@PathVariable Long id) {
        List<OrderStatusHistory> history = orderService.getOrderStatusHistory(id);
        return ResponseEntity.ok(history);
    }

}
