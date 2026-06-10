package com.finalproject.ecommerce.controller;

import com.finalproject.ecommerce.dto.auth.AuthResponse;
import com.finalproject.ecommerce.dto.auth.LoginRequest;
import com.finalproject.ecommerce.dto.auth.RegisterRequest;
import com.finalproject.ecommerce.dto.auth.VerifyOtpRequest;
import com.finalproject.ecommerce.service.interfaces.AuthService;
import com.finalproject.ecommerce.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final SecurityUtils securityUtils;

	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
		String message = authService.register(request);
		return ResponseEntity.ok(Map.of("message", message));
	}

	@PostMapping("/verify-email")
	public ResponseEntity<AuthResponse> verifyEmail(@Valid @RequestBody VerifyOtpRequest request) {
		return ResponseEntity.ok(authService.verifyEmail(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@GetMapping("/me")
	public ResponseEntity<AuthResponse> me() {
		var user = securityUtils.getCurrentUser();
		return ResponseEntity.ok(AuthResponse.builder()
				.userId(user.getId())
				.name(user.getName())
				.surname(user.getSurname())
				.username(user.getUsername())
				.email(user.getEmail())
				.role(user.getRole())
				.build());
	}
}
