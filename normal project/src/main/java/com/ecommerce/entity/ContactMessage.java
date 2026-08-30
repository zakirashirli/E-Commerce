package com.ecommerce.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="contact_messages")
public class ContactMessage {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name; @Column(nullable=false) private String email; private String phone;
    @Column(nullable=false,length=3000) private String message; @Column(nullable=false) private LocalDateTime createdAt=LocalDateTime.now();
    public void setName(String v){name=v;} public void setEmail(String v){email=v;} public void setPhone(String v){phone=v;} public void setMessage(String v){message=v;}
}
