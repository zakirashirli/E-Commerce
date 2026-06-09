package com.finalproject.ecommerce.service.interfaces;

import com.finalproject.ecommerce.dto.contact.ContactMessageRequest;
import com.finalproject.ecommerce.dto.contact.ContactMessageResponse;

import java.util.List;

public interface ContactService {

	ContactMessageResponse createMessage(ContactMessageRequest request);

	List<ContactMessageResponse> getAllMessages();
}
