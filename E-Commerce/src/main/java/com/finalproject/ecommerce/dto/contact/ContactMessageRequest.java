package com.finalproject.ecommerce.dto.contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactMessageRequest {

	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	private String email;

	@NotBlank(message = "Phone is required")
	private String phone;

	@NotBlank(message = "Subject is required")
	private String subject;

	@NotBlank(message = "Message is required")
	private String message;
}
