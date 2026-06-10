package com.finalproject.ecommerce.service.impl;

import com.finalproject.ecommerce.dto.auth.AuthResponse;
import com.finalproject.ecommerce.dto.auth.LoginRequest;
import com.finalproject.ecommerce.dto.auth.RegisterRequest;
import com.finalproject.ecommerce.dto.auth.VerifyOtpRequest;
import com.finalproject.ecommerce.entity.OtpCode;
import com.finalproject.ecommerce.entity.User;
import com.finalproject.ecommerce.enums.Role;
import com.finalproject.ecommerce.exception.BadRequestException;
import com.finalproject.ecommerce.exception.ResourceNotFoundException;
import com.finalproject.ecommerce.exception.UnauthorizedException;
import com.finalproject.ecommerce.repository.OtpCodeRepository;
import com.finalproject.ecommerce.repository.UserRepository;
import com.finalproject.ecommerce.security.JwtService;
import com.finalproject.ecommerce.service.interfaces.AuthService;
import com.finalproject.ecommerce.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final OtpCodeRepository otpCodeRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final EmailService emailService;

	@Value("${app.auth.auto-verify:false}")
	private boolean autoVerify;

	@Override
	@Transactional
	public String register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BadRequestException("Email is already registered");
		}
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new BadRequestException("Username is already taken");
		}

		User user = User.builder()
				.name(request.getName())
				.surname(request.getSurname())
				.username(request.getUsername())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.USER)
				.enabled(autoVerify)
				.build();
		userRepository.save(user);

		if (autoVerify) {
			return "Registration successful. You can now log in.";
		}

		String otp = generateOtp();
		otpCodeRepository.save(OtpCode.builder()
				.email(request.getEmail())
				.code(otp)
				.expiresAt(LocalDateTime.now().plusMinutes(10))
				.build());
		emailService.sendOtp(request.getEmail(), otp);
		return "Registration successful. Please verify your email with the OTP sent.";
	}

	@Override
	@Transactional
	public AuthResponse verifyEmail(VerifyOtpRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (user.isEnabled()) {
			throw new BadRequestException("Email is already verified");
		}

		OtpCode otpCode = otpCodeRepository.findTopByEmailOrderByCreatedAtDesc(request.getEmail())
				.orElseThrow(() -> new BadRequestException("OTP not found. Please register again."));

		if (otpCode.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new BadRequestException("OTP has expired");
		}
		if (!otpCode.getCode().equals(request.getCode())) {
			throw new BadRequestException("Invalid OTP code");
		}

		user.setEnabled(true);
		userRepository.save(user);

		UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.authorities("ROLE_" + user.getRole().name())
				.build();

		String token = jwtService.generateToken(userDetails);
		return buildAuthResponse(user, token);
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

		if (!user.isEnabled()) {
			throw new UnauthorizedException("Email not verified. Please verify your email first.");
		}

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword()));

		UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.authorities("ROLE_" + user.getRole().name())
				.build();

		String token = jwtService.generateToken(userDetails);
		return buildAuthResponse(user, token);
	}

	private AuthResponse buildAuthResponse(User user, String token) {
		return AuthResponse.builder()
				.token(token)
				.userId(user.getId())
				.name(user.getName())
				.surname(user.getSurname())
				.username(user.getUsername())
				.email(user.getEmail())
				.role(user.getRole())
				.build();
	}

	private String generateOtp() {
		return String.format("%06d", new Random().nextInt(999999));
	}
}
