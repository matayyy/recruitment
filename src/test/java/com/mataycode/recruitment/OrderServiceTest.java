package com.mataycode.recruitment;

import com.mataycode.recruitment.domain.Order;
import com.mataycode.recruitment.repository.OrderRepository;
import com.mataycode.recruitment.services.OrderService;
import com.mataycode.recruitment.repository.OrderStatusHistoryRepository;
import com.mataycode.recruitment.repository.ProductRepository;
import com.mataycode.recruitment.domain.Status;
import com.mataycode.recruitment.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    private OrderService underTest;

//    @BeforeEach
//    void setUp() {
//        underTest = new OrderService(orderRepository, productRepository, orderStatusHistoryRepository);
//    }

    @Test
    void shouldUpdateOrderStatus() {
        //GIVEN
        Long id = 1L;
        Order order = new Order(id, Collections.emptyList(), LocalDateTime.now());

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        //WHEN
        Order actual = underTest.updateStatus(order.getId(), Status.PROCESSING);

        //THEN
        assertThat(actual.getStatus()).isEqualTo(Status.PROCESSING);
    }

    @Test
    void shouldNotAllowInvalidStatusChange() {
        //GIVEN
        Long id = 1L;
        Order order = new Order(id, Collections.emptyList(), LocalDateTime.now());

        //WHEN
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        //THEN
        assertThatThrownBy(() -> underTest.updateStatus(order.getId(), Status.DELIVERED))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cannot change status from " + order.getStatus() + " to " + Status.DELIVERED);
    }
}