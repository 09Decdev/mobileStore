package com.example.MobileStore.entity;

import jakarta.persistence.*;

@Entity
public class Conditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 25)
    private String name;
}
