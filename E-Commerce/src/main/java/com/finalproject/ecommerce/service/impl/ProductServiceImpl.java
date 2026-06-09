package com.finalproject.ecommerce.service.impl;

import com.finalproject.ecommerce.dto.common.PagedResponse;
import com.finalproject.ecommerce.dto.product.ProductRequest;
import com.finalproject.ecommerce.dto.product.ProductResponse;
import com.finalproject.ecommerce.entity.Category;
import com.finalproject.ecommerce.entity.Product;
import com.finalproject.ecommerce.entity.User;
import com.finalproject.ecommerce.exception.ResourceNotFoundException;
import com.finalproject.ecommerce.repository.CategoryRepository;
import com.finalproject.ecommerce.repository.ProductRepository;
import com.finalproject.ecommerce.service.ProductSpecification;
import com.finalproject.ecommerce.service.interfaces.ProductService;
import com.finalproject.ecommerce.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final SecurityUtils securityUtils;

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		User currentUser = securityUtils.getCurrentUser();
		Category category = findCategory(request.getCategoryId());

		Product product = Product.builder()
				.name(request.getName())
				.description(request.getDescription())
				.price(request.getPrice())
				.imageUrl(request.getImageUrl())
				.stockQuantity(request.getStockQuantity())
				.brand(request.getBrand())
				.category(category)
				.createdBy(currentUser)
				.build();

		return toResponse(productRepository.save(product));
	}

	@Override
	public ProductResponse updateProduct(Long id, ProductRequest request) {
		Product product = findProduct(id);
		Category category = findCategory(request.getCategoryId());

		product.setName(request.getName());
		product.setDescription(request.getDescription());
		product.setPrice(request.getPrice());
		product.setImageUrl(request.getImageUrl());
		product.setStockQuantity(request.getStockQuantity());
		product.setBrand(request.getBrand());
		product.setCategory(category);

		return toResponse(productRepository.save(product));
	}

	@Override
	public void deleteProduct(Long id) {
		Product product = findProduct(id);
		productRepository.delete(product);
	}

	@Override
	public ProductResponse getProductById(Long id) {
		return toResponse(findProduct(id));
	}

	@Override
	public PagedResponse<ProductResponse> getAllProducts(Pageable pageable, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String brand) {
		Specification<Product> spec = ProductSpecification.withFilters(null, categoryId, minPrice, maxPrice, brand);
		Page<Product> page = productRepository.findAll(spec, pageable);
		return toPagedResponse(page);
	}

	@Override
	public PagedResponse<ProductResponse> filterProducts(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String brand, Pageable pageable) {
		Specification<Product> spec = ProductSpecification.withFilters(name, categoryId, minPrice, maxPrice, brand);
		Page<Product> page = productRepository.findAll(spec, pageable);
		return toPagedResponse(page);
	}

	@Override
	public PagedResponse<ProductResponse> searchProducts(String name, Pageable pageable) {
		Page<Product> page = productRepository.findByNameContainingIgnoreCase(name, pageable);
		return toPagedResponse(page);
	}

	private Product findProduct(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	private Category findCategory(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
	}

	private PagedResponse<ProductResponse> toPagedResponse(Page<Product> page) {
		return PagedResponse.<ProductResponse>builder()
				.content(page.getContent().stream().map(this::toResponse).toList())
				.page(page.getNumber())
				.size(page.getSize())
				.totalElements(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.last(page.isLast())
				.build();
	}

	private ProductResponse toResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.imageUrl(product.getImageUrl())
				.stockQuantity(product.getStockQuantity())
				.brand(product.getBrand())
				.categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
				.categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
				.createdById(product.getCreatedBy() != null ? product.getCreatedBy().getId() : null)
				.createdAt(product.getCreatedAt())
				.updatedAt(product.getUpdatedAt())
				.build();
	}
}
