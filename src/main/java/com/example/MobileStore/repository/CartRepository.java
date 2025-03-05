package com.example.MobileStore.repository;

import com.example.MobileStore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findCartById(@Param("userId") Long userId);
}
