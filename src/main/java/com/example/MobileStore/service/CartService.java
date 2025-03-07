package com.example.MobileStore.service;

import com.example.MobileStore.dto.CartDTO;
import com.example.MobileStore.dto.CartItemDTO;
import com.example.MobileStore.dto.CartUpdateDTO;
import com.example.MobileStore.entity.Cart;
import com.example.MobileStore.entity.CartItems;
import com.example.MobileStore.entity.Product;
import com.example.MobileStore.entity.User;
import com.example.MobileStore.mapper.CartMapper;
import com.example.MobileStore.repository.CartItemRepository;
import com.example.MobileStore.repository.CartRepository;
import com.example.MobileStore.repository.ProductRepository;
import com.example.MobileStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
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

    public CartDTO getCartByUserID(Long userId){
        Cart cart=cartRepository.findByUserId(userId)
                .orElseThrow(()->new RuntimeException("Not Found Cart "+userId));
        return CartMapper.toDTO(cart);
    }

    public CartDTO addProductToCart(Long userId, Long productId, int quantity) {
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
        Cart cart1=cartRepository.save(cart);
        return CartMapper.toDTO(cart1);
    }

    public CartDTO deleteItemcart(Long userId,Long productID){
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
        Cart updateCart=cartRepository.save(cart);
        return CartMapper.toDTO(updateCart);
    }

    public CartDTO updateCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Not found cart with userId: " + userId));

        Optional<CartItems> optionalCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (optionalCartItem.isPresent()) {
            CartItems cartItem = optionalCartItem.get();
            int newQuantity = quantity; // hoặc cộng dồn nếu cần: cartItem.getQuantity() + quantity
            cartItem.setQuantity(newQuantity);
            BigDecimal price = BigDecimal.valueOf(cartItem.getProduct().getPrice());
            BigDecimal total = price.multiply(BigDecimal.valueOf(newQuantity));
            cartItem.setTotal(total);
        } else {
            // Nếu chưa có, có thể thêm mới CartItem (nếu cần)
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Not found product with id: " + productId));
            CartItems newCartItem = new CartItems();
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            newCartItem.setTotal(BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(quantity)));
            newCartItem.setCart(cart);
            cart.getItems().add(newCartItem);
        }

        // Không can thiệp đến collection product.images nếu không có file ảnh mới
        Cart updatedCart = cartRepository.save(cart);
        return CartMapper.toDTO(updatedCart);
    }

}
