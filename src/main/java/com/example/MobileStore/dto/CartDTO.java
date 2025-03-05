package com.example.MobileStore.dto;

import com.example.MobileStore.entity.Cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CartDTO {
    private Long id;
    private BigDecimal totalPrice;
    private LocalDateTime dateTime;
    private List<CartItemDTO> cartItems; // Chỉ trả về danh sách sản phẩm, không trả User

    public CartDTO(Cart cart) {
        this.id = cart.getId();
        this.totalPrice = cart.getTotalPrice();
        this.dateTime = cart.getDateTime();
        this.cartItems = cart.getItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }
}
