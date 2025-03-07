package com.mataycode.recruitment.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "profile_image_id_unique",
                        columnNames = "profile_image_id"
                )
        }
)
public class Customer {

    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Gender Gender;

    @Column(nullable = false)
    private String profileImageId;

    //CONSTRUCTORS
    public Customer() {
    }

    //OWN METHODS
    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Gender getGender() {
        return Gender;
    }

    public String getProfileImageId() {
        return profileImageId;
    }
}
