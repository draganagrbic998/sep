package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.PaymentRequest;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Integer> {

}
