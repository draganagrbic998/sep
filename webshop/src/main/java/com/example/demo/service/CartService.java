package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Service
@Log4j2
public class CartService {

	private final CartItemRepository repo;
	private final ProductRepository productRepo;
	private final UserService userService;

	@Transactional(readOnly = true)
	public List<CartItem> read() {
		log.info("CartService - readCart");
		return repo.findByUserId(userService.getLoggedInUser().getId());
	}

	@Transactional
	public void addToCart(Long productId) {
		log.info("CartService - addToCart: productId=" + productId);
		CartItem item = repo.findByUserIdAndProductId(userService.getLoggedInUser().getId(), productId);

		if (item == null) {
			log.info("addToCart - item is null");
			repo.save(new CartItem(userService.getLoggedInUser(), productRepo.findById(productId).get()));
		} else {
			log.info("addToCart - item not null");
			item.incQuantity();
			repo.save(item);
		}
	}

	@Transactional
	public void removeFromCart(Long productId) {
		log.info("CartService - removeFromCart: productId=" + productId);
		repo.deleteById(repo.findByUserIdAndProductId(userService.getLoggedInUser().getId(), productId).getId());
	}

}
