package com.ecommerce.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name="customer_orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="customer_id") @JsonIgnore private User customer;
    @Column(nullable=false) private String address; @Column(nullable=false) private String city; @Column(nullable=false) private String country;
    @Column(nullable=false) private String postalCode; @Column(nullable=false) private String phone; @Column(nullable=false) private double total;
    @Column(nullable=false) private LocalDateTime createdAt=LocalDateTime.now();
    @OneToMany(mappedBy="order",cascade=CascadeType.ALL,orphanRemoval=true) private List<OrderItem> items=new ArrayList<>();
    public Long getId(){return id;} public User getCustomer(){return customer;} public void setCustomer(User v){customer=v;} public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;} public String getCountry(){return country;} public void setCountry(String v){country=v;} public String getPostalCode(){return postalCode;} public void setPostalCode(String v){postalCode=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;} public double getTotal(){return total;} public void setTotal(double v){total=v;} public LocalDateTime getCreatedAt(){return createdAt;}
    public List<OrderItem> getItems(){return items;}
}
