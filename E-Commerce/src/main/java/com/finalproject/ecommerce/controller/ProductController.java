package com.finalproject.ecommerce.controller;

import com.finalproject.ecommerce.dto.common.PagedResponse;
import com.finalproject.ecommerce.dto.product.ProductRequest;
import com.finalproject.ecommerce.dto.product.ProductResponse;
import com.finalproject.ecommerce.service.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<PagedResponse<ProductResponse>> getAllProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction,
			@RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(required = false) String brand) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
		return ResponseEntity.ok(productService.getAllProducts(pageable, categoryId, minPrice, maxPrice, brand));
	}

	@GetMapping("/search")
	public ResponseEntity<PagedResponse<ProductResponse>> searchProducts(
			@RequestParam String name,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size) {

		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(productService.searchProducts(name, pageable));
	}

	@GetMapping("/filter")
	public ResponseEntity<PagedResponse<ProductResponse>> filterProducts(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Long categoryId,
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
		return ResponseEntity.ok(productService.filterProducts(name, categoryId, minPrice, maxPrice, brand, pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}
}
