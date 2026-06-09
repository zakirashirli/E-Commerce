package com.finalproject.ecommerce.dto.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessageResponse {

	private Long id;
	private String name;
	private String email;
	private String phone;
	private String subject;
	private String message;
	private LocalDateTime createdAt;
}
