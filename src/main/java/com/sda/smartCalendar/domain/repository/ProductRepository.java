package com.sda.smartCalendar.domain.repository;

import com.sda.smartCalendar.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository <Product, UUID> {

    Product findProductById(UUID id);
    void deleteProductById(UUID id);
    List<Product> findAllByUserEmail(String email);
}
