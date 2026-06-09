package com.finalproject.ecommerce.repository;

import com.finalproject.ecommerce.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}
