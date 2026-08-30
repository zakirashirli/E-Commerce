package com.ecommerce.controller;
import com.ecommerce.dto.ApiDtos.*; import com.ecommerce.service.UserService; import jakarta.validation.Valid; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {private final UserService service;public AuthController(UserService s){service=s;}@PostMapping("/register")UserResponse register(@Valid @RequestBody RegisterRequest r){return service.register(r);}@PostMapping("/login")AuthResponse login(@Valid @RequestBody LoginRequest r){return service.login(r);}@GetMapping("/me")UserResponse me(Authentication a){return service.response(service.find(a.getName()));}}
