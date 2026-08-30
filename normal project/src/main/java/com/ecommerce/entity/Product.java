package com.ecommerce.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity @Table(name="products")
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String brand;
    @Column(nullable=false) private String model;
    @Column(nullable=false) private String category;
    @Column(nullable=false,length=2000) private String description;
    @Column(nullable=false) private double price;
    @Column(nullable=false) private int rating;
    @Column(nullable=false) private String imageUrl;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="seller_id") @JsonIgnore private User seller;
    public Long getId(){return id;} public String getBrand(){return brand;} public void setBrand(String v){brand=v;} public String getModel(){return model;} public void setModel(String v){model=v;}
    public String getCategory(){return category;} public void setCategory(String v){category=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public double getPrice(){return price;} public void setPrice(double v){price=v;} public int getRating(){return rating;} public void setRating(int v){rating=v;}
    public String getImageUrl(){return imageUrl;} public void setImageUrl(String v){imageUrl=v;} public User getSeller(){return seller;} public void setSeller(User v){seller=v;}
}
