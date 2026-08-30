package com.ecommerce.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

// A distinct table name avoids the incompatible cart_items table from the
// original classroom project when an existing database is upgraded in place.
@Entity @Table(name="shopping_cart_items",uniqueConstraints=@UniqueConstraint(columnNames={"user_id","product_id"}))
public class CartItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="user_id") @JsonIgnore private User user;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="product_id") private Product product;
    @Column(nullable=false) private int quantity;
    public Long getId(){return id;} public User getUser(){return user;} public void setUser(User v){user=v;} public Product getProduct(){return product;} public void setProduct(Product v){product=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){quantity=v;}
}
