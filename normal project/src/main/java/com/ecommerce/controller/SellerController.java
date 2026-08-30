package com.ecommerce.controller;
import com.ecommerce.dto.ApiDtos.SaleResponse; import com.ecommerce.service.OrderService; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/seller")
public class SellerController {private final OrderService service;public SellerController(OrderService s){service=s;}@GetMapping("/sales")List<SaleResponse> sales(Authentication a){return service.sales(a.getName());}}
