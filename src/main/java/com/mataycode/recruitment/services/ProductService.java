package com.mataycode.recruitment.services;

import com.mataycode.recruitment.domain.Product;
import com.mataycode.recruitment.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllOrders() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.getProductById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
