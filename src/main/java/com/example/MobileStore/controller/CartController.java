package com.example.MobileStore.controller;

import com.example.MobileStore.dto.CartDTO;
import com.example.MobileStore.dto.CartUpdateDTO;
import com.example.MobileStore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        CartDTO cartDTO=cartService.getCartByUserID(userId);
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addProductToCart(@RequestParam Long userId,
                                                    @RequestParam Long productId,
                                                    @RequestParam int quantity){
        CartDTO updateCart= cartService.addProductToCart(userId,productId,quantity);
        return ResponseEntity.ok(updateCart);
    }

    @DeleteMapping("/{userId}/items/{productID}")
    public ResponseEntity<CartDTO> deleteItemCart(
            @PathVariable Long userId,
            @PathVariable Long productID
    ){
        CartDTO updateCart=cartService.deleteItemcart(userId,productID);
        return ResponseEntity.ok(updateCart);
    }

    @PutMapping("/update")
    public ResponseEntity<CartDTO> updateCart(@RequestParam Long userId,
                                              @RequestParam Long productId,
                                              @RequestParam int quantity){
       CartDTO updateItemCart=cartService.updateCart(userId,productId,quantity);
        return ResponseEntity.ok(updateItemCart);
    }
}
