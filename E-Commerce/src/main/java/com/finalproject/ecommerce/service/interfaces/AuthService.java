package com.finalproject.ecommerce.service.interfaces;

import com.finalproject.ecommerce.dto.auth.AuthResponse;
import com.finalproject.ecommerce.dto.auth.LoginRequest;
import com.finalproject.ecommerce.dto.auth.RegisterRequest;
import com.finalproject.ecommerce.dto.auth.VerifyOtpRequest;

public interface AuthService {

	String register(RegisterRequest request);

	AuthResponse verifyEmail(VerifyOtpRequest request);

	AuthResponse login(LoginRequest request);
}
