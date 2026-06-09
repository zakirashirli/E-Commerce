package com.finalproject.ecommerce.dto.order;

import com.finalproject.ecommerce.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

	@NotNull(message = "Order status is required")
	private OrderStatus status;
}
