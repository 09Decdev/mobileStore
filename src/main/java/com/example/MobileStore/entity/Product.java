package com.example.MobileStore.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int stock;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;//mieu tả

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;//nhà sản xuất

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;//loại

    @ManyToOne
    @JoinColumn(name = "condition_id")
    private Conditions condition;

    @OneToMany(mappedBy ="product",cascade = CascadeType.ALL)
    private List<ProductImage>images=new ArrayList<>();

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Conditions getCondition() {
        return condition;
    }

    public void setCondition(Conditions condition) {
        this.condition = condition;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }
}
