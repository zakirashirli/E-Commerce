package com.finalproject.ecommerce.service.impl;

import com.finalproject.ecommerce.dto.common.PagedResponse;
import com.finalproject.ecommerce.dto.order.CheckoutRequest;
import com.finalproject.ecommerce.dto.order.OrderItemResponse;
import com.finalproject.ecommerce.dto.order.OrderResponse;
import com.finalproject.ecommerce.dto.order.UpdateOrderStatusRequest;
import com.finalproject.ecommerce.entity.*;
import com.finalproject.ecommerce.enums.OrderStatus;
import com.finalproject.ecommerce.exception.BadRequestException;
import com.finalproject.ecommerce.exception.ResourceNotFoundException;
import com.finalproject.ecommerce.repository.CartRepository;
import com.finalproject.ecommerce.repository.OrderRepository;
import com.finalproject.ecommerce.repository.ProductRepository;
import com.finalproject.ecommerce.service.interfaces.CartService;
import com.finalproject.ecommerce.service.interfaces.OrderService;
import com.finalproject.ecommerce.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final CartService cartService;
	private final SecurityUtils securityUtils;

	@Override
	@Transactional
	public OrderResponse checkout(CheckoutRequest request) {
		User user = securityUtils.getCurrentUser();
		Cart cart = cartRepository.findByUser(user)
				.orElseThrow(() -> new BadRequestException("Cart is empty"));

		if (cart.getItems().isEmpty()) {
			throw new BadRequestException("Cannot checkout empty cart");
		}

		for (CartItem item : cart.getItems()) {
			if (item.getQuantity() > item.getProduct().getStockQuantity()) {
				throw new BadRequestException("Insufficient stock for product: " + item.getProduct().getName());
			}
		}

		List<OrderItem> orderItems = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;

		for (CartItem cartItem : cart.getItems()) {
			Product product = cartItem.getProduct();
			product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
			productRepository.save(product);

			OrderItem orderItem = OrderItem.builder()
					.productName(product.getName())
					.price(product.getPrice())
					.quantity(cartItem.getQuantity())
					.build();
			orderItems.add(orderItem);
			total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
		}

		Order order = Order.builder()
				.user(user)
				.status(OrderStatus.PENDING)
				.totalAmount(total)
				.fullName(request.getFullName())
				.phone(request.getPhone())
				.email(request.getEmail())
				.address(request.getAddress())
				.city(request.getCity())
				.country(request.getCountry())
				.postalCode(request.getPostalCode())
				.paymentMethod(request.getPaymentMethod())
				.build();

		for (OrderItem orderItem : orderItems) {
			orderItem.setOrder(order);
			order.getItems().add(orderItem);
		}

		Order savedOrder = orderRepository.save(order);
		cartService.clearCart();

		return toResponse(savedOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getMyOrders() {
		User user = securityUtils.getCurrentUser();
		return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResponse<OrderResponse> getAllOrders(Pageable pageable) {
		Page<Order> page = orderRepository.findAll(pageable);
		return PagedResponse.<OrderResponse>builder()
				.content(page.getContent().stream().map(this::toResponse).toList())
				.page(page.getNumber())
				.size(page.getSize())
				.totalElements(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.last(page.isLast())
				.build();
	}

	@Override
	@Transactional
	public OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
		order.setStatus(request.getStatus());
		return toResponse(orderRepository.save(order));
	}

	private OrderResponse toResponse(Order order) {
		List<OrderItemResponse> items = order.getItems().stream()
				.map(item -> OrderItemResponse.builder()
						.id(item.getId())
						.productName(item.getProductName())
						.price(item.getPrice())
						.quantity(item.getQuantity())
						.build())
				.toList();

		return OrderResponse.builder()
				.id(order.getId())
				.status(order.getStatus())
				.totalAmount(order.getTotalAmount())
				.fullName(order.getFullName())
				.phone(order.getPhone())
				.email(order.getEmail())
				.address(order.getAddress())
				.city(order.getCity())
				.country(order.getCountry())
				.postalCode(order.getPostalCode())
				.paymentMethod(order.getPaymentMethod())
				.createdAt(order.getCreatedAt())
				.items(items)
				.build();
	}
}
