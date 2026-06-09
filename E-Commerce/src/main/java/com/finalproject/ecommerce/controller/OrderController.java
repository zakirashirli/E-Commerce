package com.finalproject.ecommerce.controller;

import com.finalproject.ecommerce.dto.common.PagedResponse;
import com.finalproject.ecommerce.dto.order.CheckoutRequest;
import com.finalproject.ecommerce.dto.order.OrderResponse;
import com.finalproject.ecommerce.dto.order.UpdateOrderStatusRequest;
import com.finalproject.ecommerce.service.interfaces.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/checkout")
	public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.checkout(request));
	}

	@GetMapping("/my")
	public ResponseEntity<List<OrderResponse>> getMyOrders() {
		return ResponseEntity.ok(orderService.getMyOrders());
	}

	@GetMapping
	public ResponseEntity<PagedResponse<OrderResponse>> getAllOrders(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
		return ResponseEntity.ok(orderService.getAllOrders(pageable));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<OrderResponse> updateOrderStatus(
			@PathVariable Long id,
			@Valid @RequestBody UpdateOrderStatusRequest request) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
	}
}
