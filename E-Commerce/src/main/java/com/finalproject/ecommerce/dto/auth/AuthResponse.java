package com.finalproject.ecommerce.dto.auth;

import com.finalproject.ecommerce.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

	private String token;
	private Long userId;
	private String username;
	private String email;
	private Role role;
}
