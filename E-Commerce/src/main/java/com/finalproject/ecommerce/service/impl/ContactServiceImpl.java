package com.finalproject.ecommerce.service.impl;

import com.finalproject.ecommerce.dto.contact.ContactMessageRequest;
import com.finalproject.ecommerce.dto.contact.ContactMessageResponse;
import com.finalproject.ecommerce.entity.ContactMessage;
import com.finalproject.ecommerce.repository.ContactMessageRepository;
import com.finalproject.ecommerce.service.interfaces.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

	private final ContactMessageRepository contactMessageRepository;

	@Override
	public ContactMessageResponse createMessage(ContactMessageRequest request) {
		ContactMessage message = ContactMessage.builder()
				.name(request.getName())
				.email(request.getEmail())
				.phone(request.getPhone())
				.subject(request.getSubject())
				.message(request.getMessage())
				.build();
		return toResponse(contactMessageRepository.save(message));
	}

	@Override
	public List<ContactMessageResponse> getAllMessages() {
		return contactMessageRepository.findAll().stream().map(this::toResponse).toList();
	}

	private ContactMessageResponse toResponse(ContactMessage message) {
		return ContactMessageResponse.builder()
				.id(message.getId())
				.name(message.getName())
				.email(message.getEmail())
				.phone(message.getPhone())
				.subject(message.getSubject())
				.message(message.getMessage())
				.createdAt(message.getCreatedAt())
				.build();
	}
}
