package com.finalproject.ecommerce.service.interfaces;

import com.finalproject.ecommerce.dto.common.PagedResponse;
import com.finalproject.ecommerce.dto.order.CheckoutRequest;
import com.finalproject.ecommerce.dto.order.OrderResponse;
import com.finalproject.ecommerce.dto.order.UpdateOrderStatusRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

	OrderResponse checkout(CheckoutRequest request);

	List<OrderResponse> getMyOrders();

	PagedResponse<OrderResponse> getAllOrders(Pageable pageable);

	OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest request);
}
