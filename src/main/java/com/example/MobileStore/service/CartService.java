package com.example.MobileStore.service;

import com.example.MobileStore.dto.CartDTO;
import com.example.MobileStore.entity.Cart;
import com.example.MobileStore.entity.CartItems;
import com.example.MobileStore.entity.Product;
import com.example.MobileStore.entity.User;
import com.example.MobileStore.repository.CartItemRepository;
import com.example.MobileStore.repository.CartRepository;
import com.example.MobileStore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Autowired
    public CartItemRepository cartItemRepository;

    public Cart getCartByUserID(Long userId){
        Cart cart=new Cart();
        if (cart==null){
            cart=new Cart();
            User user=new User();
            user.setId(userId);
            cart.setUser(user);
            cart=cartRepository.save(cart);
        }
        return cart;
    }


    public CartDTO getCart(Long userID){
        Cart cart=cartRepository.findCartById(userID);
//        cart.setTotalPrice();
        if (cart ==null) return null;
        return new CartDTO(cart);
    }

    public Cart addProductToCart(Long userId,long productId,int quality){
        Cart cart=getCartByUserID(userId);

        Product product=productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("Product not found"));

        Optional<CartItems> cartItemsOptional=cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(productId))
                .findFirst();

        if (cartItemsOptional.isPresent()){
            CartItems cartItems=cartItemsOptional.get();
            int newQuatity=cartItems.getQuantity()+quality;
            cartItems.setQuantity(newQuatity);
            cartItems.setTotal(BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(newQuatity)));
        }else {
            CartItems cartItems=new CartItems();
            cartItems.setProduct(product);
            cartItems.setQuantity(quality);
            cartItems.setTotal(BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quality)));
            cartItems.setCart(cart);
            cart.getItems().add(cartItems);
        }
        return cartRepository.save(cart);
    }

    public Cart UpdateCartItem(Long userId,Long prductId,int quantity){
        Cart cart =getCartByUserID(userId);
        cart.getItems()
                .stream()
                .filter(item->item.getProduct().getId().equals(prductId))
                .findFirst()
                .ifPresent(item->item.setQuantity(quantity));

        return  cartRepository.save(cart);
    }
}
