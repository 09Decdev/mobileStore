package com.example.MobileStore.service;

import com.example.MobileStore.entity.Cart;
import com.example.MobileStore.entity.CartItems;
import com.example.MobileStore.entity.Product;
import com.example.MobileStore.entity.User;
import com.example.MobileStore.repository.CartItemRepository;
import com.example.MobileStore.repository.CartRepository;
import com.example.MobileStore.repository.ProductRepository;
import com.example.MobileStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    public CartItemRepository cartItemRepository;

    public Cart getCartByUserID(Long userId){
        return cartRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("Not Found Cart "+userId));
    }

    public Cart addProductToCart(Long userId, Long productId, int quantity) {
        //Lấy user từ db
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));

        // Tìm giỏ hàng theo userId, nếu không tìm thấy thì tạo mới
        Cart cart=cartRepository.findByUserId(userId)
                .orElseGet(()->{
                    Cart newCart=new Cart();
                    newCart.setUser(user);
                    return  newCart;
                });

        //tim poduct
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("Not found product"));

        Optional<CartItems> existingItemOpt=cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst();
        if (existingItemOpt.isPresent()){
            CartItems items=existingItemOpt.get();
            int newQuantity=items.getQuantity()+quantity;
            items.setQuantity(newQuantity);
            items.setTotal(BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quantity)));
        }else {
            CartItems newCartItem=new CartItems();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setTotal(BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quantity)));
            newCartItem.setCart(cart);
            cart.getItems().add(newCartItem);
        }
        return cartRepository.save(cart);
    }

    public Cart UpdateCartItem(Long userId, Long prductId, int quantity){
        Cart cart =getCartByUserID(userId);
        cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(prductId))
                .findFirst()
                .ifPresent(item->item.setQuantity(quantity));

        return  cartRepository.save(cart);
    }

    public Cart deleteItemcart(Long userId,Long productID){
        Cart cart=cartRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("Not found cart with userId: "+userId));
        Optional<CartItems>optionalCartItems=cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productID))
                .findFirst();
        if (optionalCartItems.isPresent()){
            cart.getItems().remove(optionalCartItems.get());
        }else {
            throw new RuntimeException("Not found product with id: "+productID);
        }
        return cartRepository.save(cart);
    }
}
