package com.example.MobileStore.service;

import com.example.MobileStore.entity.Manufacturer;
import com.example.MobileStore.repository.ManufacturerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    public Manufacturer findById(int id) {
        return manufacturerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found with ID: " + id));
    }
}
