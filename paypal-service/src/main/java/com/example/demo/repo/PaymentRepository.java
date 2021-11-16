package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Temp;

//Temp
public interface PaymentRepository extends JpaRepository<Temp, Integer> {

}
