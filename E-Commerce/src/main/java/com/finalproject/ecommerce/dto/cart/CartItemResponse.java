package com.finalproject.ecommerce.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

	private Long id;
	private Long itemId;
	private Long productId;
	private String productName;
	private String imageUrl;
	private BigDecimal price;
	private Integer quantity;
	private BigDecimal subtotal;
}
