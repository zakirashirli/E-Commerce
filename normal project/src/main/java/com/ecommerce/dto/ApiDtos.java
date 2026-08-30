package com.ecommerce.dto;
import com.ecommerce.entity.Role;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime; import java.util.List;

public final class ApiDtos {
 private ApiDtos(){}
 public record RegisterRequest(@NotBlank String firstName,@NotBlank String lastName,@Email @NotBlank String email,@NotBlank @Size(min=3,max=30) String username,@NotBlank @Size(min=6) String password,@NotNull Role role){}
 public record LoginRequest(@NotBlank String username,@NotBlank String password){}
 public record UserResponse(Long id,String firstName,String lastName,String email,String username,Role role){}
 public record AuthResponse(String token,UserResponse user){}
 public record ProductRequest(@NotBlank String brand,@NotBlank String model,@NotBlank String category,@NotBlank @Size(max=2000) String description,@Positive double price,@Min(0) @Max(5) int rating,@NotBlank String imageUrl){}
 public record ProductResponse(Long id,String brand,String model,String category,String description,double price,int rating,String imageUrl,String sellerUsername){}
 public record CartRequest(@NotNull Long productId,@Min(1) @Max(99) int quantity){}
 public record CartResponse(Long id,ProductResponse product,int quantity,double subtotal){}
 public record CheckoutRequest(@NotBlank String address,@NotBlank String city,@NotBlank String country,@NotBlank String postalCode,@NotBlank String phone){}
 public record OrderItemResponse(Long id,Long productId,String productName,String imageUrl,double price,int quantity,String sellerUsername){}
 public record OrderResponse(Long id,String address,String city,String country,String postalCode,String phone,double total,LocalDateTime createdAt,List<OrderItemResponse> items){}
 public record SaleResponse(Long orderItemId,String productName,String imageUrl,double price,int quantity,String customerUsername,LocalDateTime orderedAt){}
 public record ContactRequest(@NotBlank String name,@Email @NotBlank String email,String phone,@NotBlank @Size(max=3000) String message){}
}
