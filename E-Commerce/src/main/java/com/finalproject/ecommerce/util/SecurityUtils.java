package com.finalproject.ecommerce.util;

import com.finalproject.ecommerce.entity.User;
import com.finalproject.ecommerce.exception.UnauthorizedException;
import com.finalproject.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

	private final UserRepository userRepository;

	public User getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UnauthorizedException("User not authenticated"));
	}
}
