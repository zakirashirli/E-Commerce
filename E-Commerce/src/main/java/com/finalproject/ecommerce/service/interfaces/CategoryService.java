package com.finalproject.ecommerce.service.interfaces;

import com.finalproject.ecommerce.dto.category.CategoryRequest;
import com.finalproject.ecommerce.dto.category.CategoryResponse;

import java.util.List;

public interface CategoryService {

	CategoryResponse createCategory(CategoryRequest request);

	CategoryResponse updateCategory(Long id, CategoryRequest request);

	void deleteCategory(Long id);

	List<CategoryResponse> getAllCategories();
}
