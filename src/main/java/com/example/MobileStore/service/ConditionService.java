package com.example.MobileStore.service;

import com.example.MobileStore.entity.Conditions;
import com.example.MobileStore.repository.ConditionRepository;
import jakarta.persistence.Access;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConditionService {
    @Autowired
    private ConditionRepository conditionRepository;

    public Conditions findById(int id){
        return conditionRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Not Found condition"));
    }
}
