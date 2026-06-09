package com.finalproject.ecommerce.dto.order;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckoutRequest {

	@NotBlank(message = "Full name is required")
	private String fullName;

	@NotBlank(message = "Phone is required")
	private String phone;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	private String email;

	@NotBlank(message = "Address is required")
	private String address;

	@NotBlank(message = "City is required")
	private String city;

	@NotBlank(message = "Country is required")
	private String country;

	@NotBlank(message = "Postal code is required")
	private String postalCode;

	@NotBlank(message = "Payment method is required")
	private String paymentMethod;
}
