package com.coding.task.store.repository;

import com.coding.task.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByDescriptionIgnoreCase(String description);
}
