package com.ecommerce.service;
import com.ecommerce.dto.ApiDtos.*; import com.ecommerce.entity.*; import com.ecommerce.repository.UserRepository; import com.ecommerce.security.JwtService; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service;
@Service
public class UserService {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt; public UserService(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 public UserResponse register(RegisterRequest r){if(users.existsByUsername(r.username()))throw new IllegalArgumentException("Username already exists");if(users.existsByEmail(r.email()))throw new IllegalArgumentException("Email already exists");User u=new User();u.setFirstName(r.firstName());u.setLastName(r.lastName());u.setEmail(r.email());u.setUsername(r.username());u.setPassword(encoder.encode(r.password()));u.setRole(r.role());return response(users.save(u));}
 public AuthResponse login(LoginRequest r){User u=find(r.username());if(!encoder.matches(r.password(),u.getPassword()))throw new IllegalArgumentException("Invalid username or password");return new AuthResponse(jwt.create(u.getUsername()),response(u));}
 public User find(String username){return users.findByUsername(username).orElseThrow(()->new IllegalArgumentException("User not found"));}
 public UserResponse response(User u){return new UserResponse(u.getId(),u.getFirstName(),u.getLastName(),u.getEmail(),u.getUsername(),u.getRole());}
}
