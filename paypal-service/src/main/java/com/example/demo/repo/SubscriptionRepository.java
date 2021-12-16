package com.example.demo.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	List<Subscription> findAllByStatus(String status);

	Optional<Subscription> findBySubscriptionId(String subscriptionId);

	Optional<Subscription> findByPlanId(String planId);

}
