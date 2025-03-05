package com.example.MobileStore.repository;

import com.example.MobileStore.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItems,Long> {
    List<CartItems> findByCartId(Long cartId);
}
