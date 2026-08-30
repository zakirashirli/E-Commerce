package com.ecommerce.service;
import com.ecommerce.dto.ApiDtos.*; import com.ecommerce.entity.*; import com.ecommerce.repository.CartItemRepository; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List;
@Service
public class CartService {
 private final CartItemRepository carts; private final UserService users; private final ProductService products; public CartService(CartItemRepository c,UserService u,ProductService p){carts=c;users=u;products=p;}
 @Transactional(readOnly=true) public List<CartResponse> list(String n){return carts.findByUserOrderById(users.find(n)).stream().map(this::response).toList();}
 @Transactional public CartResponse add(String n,CartRequest r){User u=users.find(n);Product p=products.raw(r.productId());CartItem item=carts.findByUserAndProduct(u,p).orElseGet(()->{CartItem x=new CartItem();x.setUser(u);x.setProduct(p);x.setQuantity(0);return x;});item.setQuantity(Math.min(99,item.getQuantity()+r.quantity()));return response(carts.save(item));}
 @Transactional public CartResponse update(String n,Long id,int q){if(q<1||q>99)throw new IllegalArgumentException("Quantity must be between 1 and 99");CartItem i=owned(n,id);i.setQuantity(q);return response(carts.save(i));}
 @Transactional public void delete(String n,Long id){carts.delete(owned(n,id));} @Transactional public void clear(User u){carts.deleteByUser(u);}
 public List<CartItem> raw(User u){return carts.findByUserOrderById(u);} private CartItem owned(String n,Long id){CartItem i=carts.findById(id).orElseThrow(()->new IllegalArgumentException("Cart item not found"));if(!i.getUser().getUsername().equals(n))throw new IllegalArgumentException("Access denied");return i;}
 private CartResponse response(CartItem i){return new CartResponse(i.getId(),products.response(i.getProduct()),i.getQuantity(),i.getProduct().getPrice()*i.getQuantity());}
}
