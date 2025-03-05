package com.example.MobileStore.entity;

import jakarta.persistence.*;
@Entity
@Table(name ="product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // Lưu đường dẫn ảnh

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(Long id) {
        this.id = id;
    }

    public ProductImage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
