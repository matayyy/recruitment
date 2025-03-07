package com.mataycode.recruitment.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @SequenceGenerator(
            name = "order_items_id_seq",
            sequenceName = "order_items_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_items_id_seq"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
//    @JsonIgnore
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    //CONSTRUCTORS
    public OrderItem() {
    }

    public OrderItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    //OWN METHODS
    public void setOrder(Order order) {
        this.order = order;
    }

    //GETTERS AND SETTERS
    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    //OTHER METHODS
    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", order=" + order +
//                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
