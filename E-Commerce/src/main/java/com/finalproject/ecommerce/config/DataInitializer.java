package com.finalproject.ecommerce.config;

import com.finalproject.ecommerce.entity.Category;
import com.finalproject.ecommerce.entity.Product;
import com.finalproject.ecommerce.entity.User;
import com.finalproject.ecommerce.enums.Role;
import com.finalproject.ecommerce.repository.CategoryRepository;
import com.finalproject.ecommerce.repository.ProductRepository;
import com.finalproject.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		if (!userRepository.existsByEmail("admin@gmail.com")) {
			User admin = User.builder()
					.name("Admin")
					.surname("User")
					.username("admin")
					.email("admin@gmail.com")
					.password(passwordEncoder.encode("123456"))
					.role(Role.ADMIN)
					.enabled(true)
					.build();
			userRepository.save(admin);
		}

		if (categoryRepository.count() == 0) {
			Category phones = categoryRepository.save(Category.builder()
					.name("Phones")
					.description("Smartphones and mobile phones")
					.build());
			Category laptops = categoryRepository.save(Category.builder()
					.name("Laptops")
					.description("Laptops and notebooks")
					.build());
			Category accessories = categoryRepository.save(Category.builder()
					.name("Accessories")
					.description("Headphones, chargers, cables and accessories")
					.build());

			User admin = userRepository.findByEmail("admin@gmail.com").orElse(null);

			productRepository.save(Product.builder()
					.name("iPhone 15")
					.description("Latest Apple smartphone")
					.price(new BigDecimal("999.99"))
					.imageUrl("https://example.com/iphone15.jpg")
					.stockQuantity(50)
					.brand("Apple")
					.category(phones)
					.createdBy(admin)
					.build());

			productRepository.save(Product.builder()
					.name("MacBook Pro")
					.description("Powerful Apple laptop")
					.price(new BigDecimal("1999.99"))
					.imageUrl("https://example.com/macbook.jpg")
					.stockQuantity(30)
					.brand("Apple")
					.category(laptops)
					.createdBy(admin)
					.build());

			productRepository.save(Product.builder()
					.name("AirPods Pro")
					.description("Wireless earbuds with noise cancellation")
					.price(new BigDecimal("249.99"))
					.imageUrl("https://example.com/airpods.jpg")
					.stockQuantity(100)
					.brand("Apple")
					.category(accessories)
					.createdBy(admin)
					.build());
		}
	}
}
