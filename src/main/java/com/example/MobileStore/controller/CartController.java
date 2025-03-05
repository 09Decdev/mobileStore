package com.example.MobileStore.controller;

import com.example.MobileStore.dto.CartDTO;
import com.example.MobileStore.entity.Cart;
import com.example.MobileStore.entity.CartItems;
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

//    @GetMapping("/{userId}")
//    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId){
//        Cart cart=cartService.getCartByUserID(userId);
//        return ResponseEntity.ok(cart);
//    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<CartItems> getCartItemByUserId(@PathVariable Long userId){
//       List<CartItems> cartItems=cartService.getCartItemByUserId(userId).stream().map(cart::getCart).toList();
//        return ;
//    }
//@GetMapping("/{userId}")
//public ResponseEntity<List<CartItems>> getCartItemsByUserId(@PathVariable Long userId) {
//    // Lấy danh sách cart items theo userId từ service (đã định nghĩa truy vấn theo quan hệ)
//    List<CartItems> cartItems = cartService.getCartItemByUserId(userId);
//
//    // Loại bỏ thông tin Cart lồng nhau bằng cách đặt null cho thuộc tính cart của mỗi CartItems
//    cartItems.forEach(item -> item.setCart(null));
//
//    return ResponseEntity.ok(cartItems);
//}

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long userId) {
        CartDTO cartItems=cartService.getCart(userId);
        return ResponseEntity.ok(cartItems);
    }


    @PostMapping("/add")
    public ResponseEntity<Cart>addProductToCart(@RequestParam Long userId,
                                                @RequestParam Long productId,
                                                @RequestParam int quantity){
        Cart updateCart= cartService.addProductToCart(userId,productId,quantity);
        return ResponseEntity.ok(updateCart);
    }

}
