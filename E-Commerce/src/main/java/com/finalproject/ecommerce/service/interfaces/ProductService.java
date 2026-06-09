package com.finalproject.ecommerce.service.interfaces;

import com.finalproject.ecommerce.dto.common.PagedResponse;
import com.finalproject.ecommerce.dto.product.ProductRequest;
import com.finalproject.ecommerce.dto.product.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {

	ProductResponse createProduct(ProductRequest request);

	ProductResponse updateProduct(Long id, ProductRequest request);

	void deleteProduct(Long id);

	ProductResponse getProductById(Long id);

	PagedResponse<ProductResponse> getAllProducts(Pageable pageable, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String brand);

	PagedResponse<ProductResponse> filterProducts(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String brand, Pageable pageable);

	PagedResponse<ProductResponse> searchProducts(String name, Pageable pageable);
}
