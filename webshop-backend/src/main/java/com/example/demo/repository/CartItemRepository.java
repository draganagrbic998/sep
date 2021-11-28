package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCartId(Long cartId);

	List<CartItem> findByOrderId(Long orderId);

	CartItem findByCartIdAndProductId(Long cartId, Long productId);

}
