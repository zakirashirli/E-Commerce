package com.finalproject.ecommerce.service;

import com.finalproject.ecommerce.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

	private ProductSpecification() {
	}

	public static Specification<Product> withFilters(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, String brand) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (name != null && !name.isBlank()) {
				predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
			}
			if (categoryId != null) {
				predicates.add(cb.equal(root.get("category").get("id"), categoryId));
			}
			if (minPrice != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
			}
			if (maxPrice != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
			}
			if (brand != null && !brand.isBlank()) {
				predicates.add(cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
