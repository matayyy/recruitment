package com.mataycode.recruitment.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @SequenceGenerator(
            name = "products_id_seq",
            sequenceName = "products_id_seq"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "products_id_seq"
    )
    private Long id;

    String name;

    private Double price;

    //CONSTRUCTORS
    public Product() {
    }

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    //GETTERS AND SETTERS
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}
