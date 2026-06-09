package com.finalproject.ecommerce.service.impl;

import com.finalproject.ecommerce.dto.category.CategoryRequest;
import com.finalproject.ecommerce.dto.category.CategoryResponse;
import com.finalproject.ecommerce.entity.Category;
import com.finalproject.ecommerce.exception.ResourceNotFoundException;
import com.finalproject.ecommerce.repository.CategoryRepository;
import com.finalproject.ecommerce.service.interfaces.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public CategoryResponse createCategory(CategoryRequest request) {
		Category category = Category.builder()
				.name(request.getName())
				.description(request.getDescription())
				.build();
		return toResponse(categoryRepository.save(category));
	}

	@Override
	public CategoryResponse updateCategory(Long id, CategoryRequest request) {
		Category category = findCategory(id);
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		return toResponse(categoryRepository.save(category));
	}

	@Override
	public void deleteCategory(Long id) {
		Category category = findCategory(id);
		categoryRepository.delete(category);
	}

	@Override
	public List<CategoryResponse> getAllCategories() {
		return categoryRepository.findAll().stream().map(this::toResponse).toList();
	}

	private Category findCategory(Long id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
	}

	private CategoryResponse toResponse(Category category) {
		return CategoryResponse.builder()
				.id(category.getId())
				.name(category.getName())
				.description(category.getDescription())
				.createdAt(category.getCreatedAt())
				.build();
	}
}
