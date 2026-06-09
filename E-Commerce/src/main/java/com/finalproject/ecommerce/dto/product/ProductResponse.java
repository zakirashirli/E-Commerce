package com.finalproject.ecommerce.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private String imageUrl;
	private Integer stockQuantity;
	private String brand;
	private Long categoryId;
	private String categoryName;
	private Long createdById;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
