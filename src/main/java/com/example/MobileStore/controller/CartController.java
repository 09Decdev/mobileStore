package com.example.MobileStore.controller;

import com.example.MobileStore.dto.CartDTO;
import com.example.MobileStore.entity.Cart;
import com.example.MobileStore.mapper.CartMapper;
import com.example.MobileStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        Cart cartItems=cartService.getCartByUserID(userId);
        CartDTO cartDTO=CartMapper.toDTO(cartItems);
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart>addProductToCart(@RequestParam Long userId,
                                                @RequestParam Long productId,
                                                @RequestParam int quantity){
        Cart updateCart= cartService.addProductToCart(userId,productId,quantity);
        return ResponseEntity.ok(updateCart);
    }

    @DeleteMapping("/{userId}/items/{productID}")
    public ResponseEntity<Cart>deleteItemCart(
            @PathVariable Long userId,
            @PathVariable Long productID
    ){
        Cart updateCart=cartService.deleteItemcart(userId,productID);
        return ResponseEntity.ok(updateCart);
    }
}
