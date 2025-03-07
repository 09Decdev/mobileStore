package com.example.MobileStore.repository;

import com.example.MobileStore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Integer> {
    Cart findCartById(@Param("userId") Long userId);

    Optional<Cart> findByUserId(Long userId);
}
