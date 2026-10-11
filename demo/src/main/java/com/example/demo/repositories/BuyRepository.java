package com.example.demo.repositories;

import com.example.demo.model.Buy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyRepository extends JpaRepository<Buy,Integer> {
}
