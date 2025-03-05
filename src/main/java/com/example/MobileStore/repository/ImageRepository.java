package com.example.MobileStore.repository;

import com.example.MobileStore.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ProductImage,Long> {

}
