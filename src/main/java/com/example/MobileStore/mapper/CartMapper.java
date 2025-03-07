package com.example.MobileStore.mapper;

import com.example.MobileStore.dto.CartDTO;
import com.example.MobileStore.dto.CartItemDTO;
import com.example.MobileStore.entity.Cart;
import com.example.MobileStore.entity.CartItems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartMapper {
    public static CartDTO toDTO(Cart cart){
        if (cart==null) return null;

        CartDTO dto=new CartDTO();
        dto.setId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setDateTime(cart.getDateTime());
        List<CartItemDTO>itemDTOS=new ArrayList<>();
        if (cart.getItems()!=null){
            for (CartItems items:cart.getItems()){
                CartItemDTO cartItemDTO=new CartItemDTO();
                if (items.getProduct()!=null){
                    cartItemDTO.setId(items.getProduct().getId());
                    cartItemDTO.setProductName(items.getProduct().getName());

//                    BigDecimal price= BigDecimal.valueOf(items.getProduct().getPrice());
//                    BigDecimal total=price.multiply(BigDecimal.valueOf(items.getQuantity()));
//                    cartItemDTO.setTotal(total);
                    double price = items.getProduct().getPrice();
                    double total = price * items.getQuantity();
                    cartItemDTO.setTotal(BigDecimal.valueOf(total));

                }
                cartItemDTO.setQuantity(items.getQuantity());
                itemDTOS.add(cartItemDTO);
            }
        }

        dto.setCartItems(itemDTOS);
        return dto;
    }
}
