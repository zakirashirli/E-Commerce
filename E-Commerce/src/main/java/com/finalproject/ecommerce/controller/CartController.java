package com.finalproject.ecommerce.controller;

import com.finalproject.ecommerce.dto.cart.AddToCartRequest;
import com.finalproject.ecommerce.dto.cart.CartResponse;
import com.finalproject.ecommerce.dto.cart.UpdateCartItemRequest;
import com.finalproject.ecommerce.service.interfaces.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@GetMapping
	public ResponseEntity<CartResponse> getCart() {
		return ResponseEntity.ok(cartService.getCurrentUserCart());
	}

	@PostMapping("/items")
	public ResponseEntity<CartResponse> addItem(@Valid @RequestBody AddToCartRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(request));
	}

	@PutMapping("/items/{itemId}")
	public ResponseEntity<CartResponse> updateItem(@PathVariable Long itemId, @Valid @RequestBody UpdateCartItemRequest request) {
		return ResponseEntity.ok(cartService.updateItem(itemId, request));
	}

	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<CartResponse> removeItem(@PathVariable Long itemId) {
		return ResponseEntity.ok(cartService.removeItem(itemId));
	}

	@DeleteMapping("/clear")
	public ResponseEntity<Void> clearCart() {
		cartService.clearCart();
		return ResponseEntity.noContent().build();
	}
}
