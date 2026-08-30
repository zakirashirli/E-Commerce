package com.ecommerce.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity @Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="order_id") @JsonIgnore private CustomerOrder order;
    private Long productId; @Column(nullable=false) private Long sellerId; @Column(nullable=false) private String sellerUsername;
    @Column(nullable=false) private String productName; @Column(nullable=false) private String imageUrl; @Column(nullable=false) private String customerUsername; @Column(nullable=false) private java.time.LocalDateTime orderedAt;
    @Column(nullable=false) private double price; @Column(nullable=false) private int quantity;
    public Long getId(){return id;} public void setOrder(CustomerOrder v){order=v;} public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public Long getSellerId(){return sellerId;} public void setSellerId(Long v){sellerId=v;} public String getSellerUsername(){return sellerUsername;} public void setSellerUsername(String v){sellerUsername=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;} public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;}
    public String getCustomerUsername(){return customerUsername;} public void setCustomerUsername(String v){customerUsername=v;} public java.time.LocalDateTime getOrderedAt(){return orderedAt;} public void setOrderedAt(java.time.LocalDateTime v){orderedAt=v;}
    public double getPrice(){return price;} public void setPrice(double v){price=v;} public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
}
