package com.finalproject.ecommerce.service.interfaces;

import com.finalproject.ecommerce.dto.cart.AddToCartRequest;
import com.finalproject.ecommerce.dto.cart.CartResponse;
import com.finalproject.ecommerce.dto.cart.UpdateCartItemRequest;

public interface CartService {

	CartResponse getCurrentUserCart();

	CartResponse addItem(AddToCartRequest request);

	CartResponse updateItem(Long itemId, UpdateCartItemRequest request);

	CartResponse removeItem(Long itemId);

	void clearCart();
}
