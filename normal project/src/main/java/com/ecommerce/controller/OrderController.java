package com.ecommerce.controller;
import com.ecommerce.dto.ApiDtos.*; import com.ecommerce.service.OrderService; import jakarta.validation.Valid; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/orders")
public class OrderController {private final OrderService service;public OrderController(OrderService s){service=s;}@PostMapping OrderResponse place(Authentication a,@Valid @RequestBody CheckoutRequest r){return service.place(a.getName(),r);}@GetMapping("/my")List<OrderResponse> mine(Authentication a){return service.mine(a.getName());}}
