package com.ecommerce.repository;
import com.ecommerce.entity.*; import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ProductRepository extends JpaRepository<Product,Long>{ Page<Product> findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(String brand,String model,Pageable pageable); List<Product> findBySellerOrderByIdDesc(User seller); }
