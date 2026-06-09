package com.finalproject.ecommerce.service.impl;

import com.finalproject.ecommerce.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;

	@Override
	public void sendOtp(String email, String code) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email);
			message.setSubject("Email Verification OTP");
			message.setText("Your verification code is: " + code + "\nThis code expires in 10 minutes.");
			mailSender.send(message);
		} catch (Exception ex) {
			// Fallback: log OTP to console when Mailtrap is not configured
			log.warn("Failed to send email to {}. OTP code (console fallback): {}", email, code);
		}
	}
}
