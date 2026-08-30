package com.ecommerce.repository;
import com.ecommerce.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface CartItemRepository extends JpaRepository<CartItem,Long>{ List<CartItem> findByUserOrderById(User user); Optional<CartItem> findByUserAndProduct(User user,Product product); void deleteByUser(User user); void deleteByProduct(Product product); }
