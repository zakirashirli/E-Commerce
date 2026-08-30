package com.ecommerce.controller;
import com.ecommerce.dto.ApiDtos.ContactRequest; import com.ecommerce.entity.ContactMessage; import com.ecommerce.repository.ContactMessageRepository; import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*; import java.util.Map;
@RestController
public class ContactController {private final ContactMessageRepository repo;public ContactController(ContactMessageRepository r){repo=r;}@PostMapping("/api/contact")Map<String,String> contact(@Valid @RequestBody ContactRequest r){ContactMessage m=new ContactMessage();m.setName(r.name());m.setEmail(r.email());m.setPhone(r.phone());m.setMessage(r.message());repo.save(m);return Map.of("message","Message received");}@GetMapping("/health")String health(){return "OK";}}
