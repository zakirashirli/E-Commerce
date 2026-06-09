package com.finalproject.ecommerce.repository;

import com.finalproject.ecommerce.entity.Cart;
import com.finalproject.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUser(User user);
}
