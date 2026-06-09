package com.finalproject.ecommerce.controller;

import com.finalproject.ecommerce.dto.contact.ContactMessageRequest;
import com.finalproject.ecommerce.dto.contact.ContactMessageResponse;
import com.finalproject.ecommerce.service.interfaces.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

	private final ContactService contactService;

	@PostMapping
	public ResponseEntity<ContactMessageResponse> createMessage(@Valid @RequestBody ContactMessageRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(contactService.createMessage(request));
	}

	@GetMapping
	public ResponseEntity<List<ContactMessageResponse>> getAllMessages() {
		return ResponseEntity.ok(contactService.getAllMessages());
	}
}
