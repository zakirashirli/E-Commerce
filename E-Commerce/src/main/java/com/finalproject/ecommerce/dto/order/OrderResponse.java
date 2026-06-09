package com.finalproject.ecommerce.dto.order;

import com.finalproject.ecommerce.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

	private Long id;
	private OrderStatus status;
	private BigDecimal totalAmount;
	private String fullName;
	private String phone;
	private String email;
	private String address;
	private String city;
	private String country;
	private String postalCode;
	private String paymentMethod;
	private LocalDateTime createdAt;
	private List<OrderItemResponse> items;
}
