package com.mataycode.recruitment.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id" ,nullable = false)
    @JsonBackReference
    private Order order;

    private Status oldStatus;

    private Status newStatus;

    @CreationTimestamp
    private LocalDateTime changedAt;

    //CONSTRUCTORS
    public OrderStatusHistory() {
    }

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Status getOldStatus() {
        return oldStatus;
    }

    public Status getNewStatus() {
        return newStatus;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setOldStatus(Status oldStatus) {
        this.oldStatus = oldStatus;
    }

    public void setNewStatus(Status newStatus) {
        this.newStatus = newStatus;
    }
}
