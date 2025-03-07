package com.example.MobileStore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    private LocalDateTime dateTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> items = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "cart_detail",
            joinColumns = @JoinColumn(name = "cart_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id",referencedColumnName = "id")
    )
    private List<Product> productList=new ArrayList<>();

    public Cart() {
        this.dateTime=LocalDateTime.now();
        this.totalPrice=BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return items.stream().map(item->item.getTotal())
                .reduce(BigDecimal.ZERO,BigDecimal::add);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }
}
