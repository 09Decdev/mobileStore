package com.example.MobileStore.repository;

import com.example.MobileStore.entity.Conditions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConditionRepository extends JpaRepository<Conditions,Integer> {
}
