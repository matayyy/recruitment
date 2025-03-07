package com.mataycode.recruitment.repository;

import com.mataycode.recruitment.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product getProductById(Long id);
}
