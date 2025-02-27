package com.example.MobileStore.repository;

import com.example.MobileStore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCondition(String condition);

    List<Product>findByPriceBetween(Double minPrice,Double maxPrice);
}
