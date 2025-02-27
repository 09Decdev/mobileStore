package com.example.MobileStore.entity;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "manufacturer",cascade = CascadeType.ALL)
    private List<Product>products;
}
