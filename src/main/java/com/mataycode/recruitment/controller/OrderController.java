package com.mataycode.recruitment.controller;

import com.mataycode.recruitment.domain.OrderStatusHistory;
import com.mataycode.recruitment.domain.Status;
import com.mataycode.recruitment.domain.Order;
import com.mataycode.recruitment.dto.CreateOrderRequest;
import com.mataycode.recruitment.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest createOrderRequest, Authentication authentication) {

        String email;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            email = oauthUser.getAttribute("email"); // Pobierz email z Google
        } else {
            email = authentication.getName(); // Dla użytkowników logujących się normalnie (username = email)
        }

        Order order = orderService.createOrder(email, createOrderRequest);
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
    @GetMapping("/by-username")
    public ResponseEntity<Page<Order>> getOrdersByEmail(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) Status status,
            Authentication authentication) {

        String email;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            email = oauthUser.getAttribute("email"); // Pobierz email z Google
        } else {
            email = authentication.getName(); // Dla użytkowników logujących się normalnie (username = email)
        }

        Page<Order> orders;
        String username = authentication.getName();

        if (status != null) {
            orders = orderService.getOrdersByUsernameAndStatus(email, status, PageRequest.of(page, size));
        } else {
            orders = orderService.getOrdersByUsername(email, PageRequest.of(page, size));
        }

        return ResponseEntity.ok(orders);
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
