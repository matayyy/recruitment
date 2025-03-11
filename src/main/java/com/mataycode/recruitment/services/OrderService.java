package com.mataycode.recruitment.services;

import com.mataycode.recruitment.domain.*;
import com.mataycode.recruitment.dto.CreateOrderRequest;
import com.mataycode.recruitment.exception.ResourceNotFoundException;
import com.mataycode.recruitment.repository.CustomerRepository;
import com.mataycode.recruitment.repository.OrderRepository;
import com.mataycode.recruitment.repository.OrderStatusHistoryRepository;
import com.mataycode.recruitment.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository, OrderStatusHistoryRepository orderStatusHistoryRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    public Order createOrder(String customerEmail, CreateOrderRequest createOrderRequest) {
        Customer customer = customerRepository.findCustomerByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email [%s] not found".formatted(customerEmail)));

        List<OrderItem> orderItems = new ArrayList<>();

        for (Long productId : createOrderRequest.productsIds()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id [%s] not found".formatted(productId)));
            orderItems.add(new OrderItem(product, 1));
        }

        Order order = new Order(customer, orderItems);
        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }

        return orderRepository.save(order);
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    public boolean deleteOrderById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getOrdersByUsername(String username, Pageable pageable) {
        Customer customer = customerRepository.findCustomerByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email [%s] not found".formatted(username)));
        return orderRepository.findOrdersByCustomer_Id(customer.getId(), pageable);
    }

    public Page<Order> getOrdersByStatus(Status status, Pageable pageable) {
        return orderRepository.findOrdersByStatus(status, pageable);
    }

    public Page<Order> getOrdersByUsernameAndStatus(String username, Status status, Pageable pageable) {
        Customer customer = customerRepository.findCustomerByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email [%s] not found".formatted(username)));
        return orderRepository.findOrdersByCustomer_IdAndStatus(customer.getId(), status, pageable);
    }

    private static final Map<Status, List<Status>> ALLOWED_STATUS_CHANGES = Map.of(
            Status.NEW, List.of(Status.PROCESSING, Status.CANCELLED),
            Status.PROCESSING, List.of(Status.SHIPPED, Status.CANCELLED),
            Status.SHIPPED, List.of(Status.DELIVERED),
            Status.DELIVERED, List.of(),
            Status.CANCELLED, List.of()
    );

    public Order updateStatus(Long id, Status newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found: " + id));

        if (!ALLOWED_STATUS_CHANGES.get(order.getStatus()).contains(newStatus)) {
            throw new ResourceNotFoundException("Cannot change status from " + order.getStatus() + " to " + newStatus);
        }

        //Logging and storing change status at database.
        logger.info("Order {}: Status changed from {} to {}", id, order.getStatus(), newStatus);
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setOldStatus(order.getStatus());
        history.setNewStatus(newStatus);
        orderStatusHistoryRepository.save(history);

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public List<OrderStatusHistory> getOrderStatusHistory(Long orderId) {
        return orderStatusHistoryRepository.findOrderStatusHistoriesByOrder_Id(orderId);
    }
}
