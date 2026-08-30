package com.ecommerce.service;

import com.ecommerce.dto.ApiDtos.RegisterRequest;
import com.ecommerce.entity.Role;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Test void registrationHashesPasswordAndNeverReturnsIt() {
        UserRepository users=mock(UserRepository.class); PasswordEncoder encoder=mock(PasswordEncoder.class);
        when(encoder.encode("secret12")).thenReturn("HASH"); when(users.save(any())).thenAnswer(i->i.getArgument(0));
        UserService service=new UserService(users,encoder,mock(JwtService.class));
        var response=service.register(new RegisterRequest("A","B","a@b.com","seller1","secret12", Role.SELLER));
        assertEquals("seller1",response.username()); verify(encoder).encode("secret12");
    }

    @Test void duplicateUsernameIsRejected() {
        UserRepository users=mock(UserRepository.class); when(users.existsByUsername("taken")).thenReturn(true);
        UserService service=new UserService(users,mock(PasswordEncoder.class),mock(JwtService.class));
        assertThrows(IllegalArgumentException.class,()->service.register(new RegisterRequest("A","B","a@b.com","taken","secret12",Role.BUYER)));
    }
}
