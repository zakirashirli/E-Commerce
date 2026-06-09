package com.finalproject.ecommerce.service.impl;

import com.finalproject.ecommerce.dto.cart.AddToCartRequest;
import com.finalproject.ecommerce.dto.cart.CartItemResponse;
import com.finalproject.ecommerce.dto.cart.CartResponse;
import com.finalproject.ecommerce.dto.cart.UpdateCartItemRequest;
import com.finalproject.ecommerce.entity.Cart;
import com.finalproject.ecommerce.entity.CartItem;
import com.finalproject.ecommerce.entity.Product;
import com.finalproject.ecommerce.entity.User;
import com.finalproject.ecommerce.exception.BadRequestException;
import com.finalproject.ecommerce.exception.ResourceNotFoundException;
import com.finalproject.ecommerce.repository.CartItemRepository;
import com.finalproject.ecommerce.repository.CartRepository;
import com.finalproject.ecommerce.repository.ProductRepository;
import com.finalproject.ecommerce.service.interfaces.CartService;
import com.finalproject.ecommerce.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final SecurityUtils securityUtils;

	@Override
	@Transactional(readOnly = true)
	public CartResponse getCurrentUserCart() {
		Cart cart = getOrCreateCart(securityUtils.getCurrentUser());
		return toResponse(cart);
	}

	@Override
	@Transactional
	public CartResponse addItem(AddToCartRequest request) {
		User user = securityUtils.getCurrentUser();
		Cart cart = getOrCreateCart(user);
		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

		CartItem existingItem = cart.getItems().stream()
				.filter(item -> item.getProduct().getId().equals(product.getId()))
				.findFirst()
				.orElse(null);

		int newQuantity = existingItem != null
				? existingItem.getQuantity() + request.getQuantity()
				: request.getQuantity();

		if (newQuantity > product.getStockQuantity()) {
			throw new BadRequestException("Cannot add more than available stock");
		}

		if (existingItem != null) {
			existingItem.setQuantity(newQuantity);
		} else {
			CartItem cartItem = CartItem.builder()
					.cart(cart)
					.product(product)
					.quantity(request.getQuantity())
					.build();
			cart.getItems().add(cartItem);
		}

		return toResponse(cartRepository.save(cart));
	}

	@Override
	@Transactional
	public CartResponse updateItem(Long itemId, UpdateCartItemRequest request) {
		User user = securityUtils.getCurrentUser();
		Cart cart = getOrCreateCart(user);
		CartItem item = findCartItem(cart, itemId);

		if (request.getQuantity() > item.getProduct().getStockQuantity()) {
			throw new BadRequestException("Cannot set quantity higher than available stock");
		}

		item.setQuantity(request.getQuantity());
		return toResponse(cartRepository.save(cart));
	}

	@Override
	@Transactional
	public CartResponse removeItem(Long itemId) {
		User user = securityUtils.getCurrentUser();
		Cart cart = getOrCreateCart(user);
		CartItem item = findCartItem(cart, itemId);
		cart.getItems().remove(item);
		cartItemRepository.delete(item);
		return toResponse(cartRepository.save(cart));
	}

	@Override
	@Transactional
	public void clearCart() {
		Cart cart = getOrCreateCart(securityUtils.getCurrentUser());
		cart.getItems().clear();
		cartRepository.save(cart);
	}

	private Cart getOrCreateCart(User user) {
		return cartRepository.findByUser(user)
				.orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));
	}

	private CartItem findCartItem(Cart cart, Long itemId) {
		return cart.getItems().stream()
				.filter(item -> item.getId().equals(itemId))
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));
	}

	private CartResponse toResponse(Cart cart) {
		List<CartItemResponse> items = cart.getItems().stream().map(item -> {
			BigDecimal subtotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
			return CartItemResponse.builder()
					.id(item.getId())
					.productId(item.getProduct().getId())
					.productName(item.getProduct().getName())
					.imageUrl(item.getProduct().getImageUrl())
					.price(item.getProduct().getPrice())
					.quantity(item.getQuantity())
					.subtotal(subtotal)
					.build();
		}).toList();

		BigDecimal total = items.stream()
				.map(CartItemResponse::getSubtotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return CartResponse.builder()
				.id(cart.getId())
				.items(items)
				.totalPrice(total)
				.build();
	}
}
