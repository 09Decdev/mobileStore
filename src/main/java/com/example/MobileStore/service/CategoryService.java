package com.example.MobileStore.service;

import com.example.MobileStore.entity.Category;
import com.example.MobileStore.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category findById(int id){
        return categoryRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not Found"));
    }
}
