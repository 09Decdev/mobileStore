package com.example.MobileStore.dto;

import com.example.MobileStore.entity.CartItems;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.math.BigDecimal;

public class CartItemDTO {
    private Long id;
    private String productName;
    private int quantity;
    private BigDecimal total;

    public CartItemDTO(CartItems cartItem) {
        this.id = cartItem.getId();
        this.productName = cartItem.getProduct().getName();
        this.quantity = cartItem.getQuantity();
        this.total = cartItem.getTotal();
    }

    public CartItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
