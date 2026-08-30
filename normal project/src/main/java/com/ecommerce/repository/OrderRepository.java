package com.ecommerce.repository;
import com.ecommerce.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface OrderRepository extends JpaRepository<CustomerOrder,Long>{ List<CustomerOrder> findByCustomerOrderByCreatedAtDesc(User customer); }
