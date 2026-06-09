package com.finalproject.ecommerce.repository;

import com.finalproject.ecommerce.entity.Order;
import com.finalproject.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserOrderByCreatedAtDesc(User user);

	Page<Order> findAll(Pageable pageable);
}
