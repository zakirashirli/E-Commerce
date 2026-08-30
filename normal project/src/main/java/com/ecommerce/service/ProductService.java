package com.ecommerce.service;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.dto.ApiDtos.*; import com.ecommerce.entity.*; import com.ecommerce.repository.ProductRepository; import org.springframework.data.domain.*; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List;
@Service
public class ProductService {
 private final ProductRepository products; private final CartItemRepository carts; private final UserService users; public ProductService(ProductRepository p,CartItemRepository c,UserService u){products=p;carts=c;users=u;}
 @Transactional(readOnly=true) public Page<ProductResponse> all(String q,String category,int page,int size,String sort){Sort s="price-desc".equals(sort)?Sort.by("price").descending():"price-asc".equals(sort)?Sort.by("price").ascending():Sort.by("id").descending();Pageable pageable=PageRequest.of(Math.max(0,page),Math.min(Math.max(size,1),100),s);Page<Product> result=(q==null||q.isBlank())?products.findAll(pageable):products.findByBrandContainingIgnoreCaseOrModelContainingIgnoreCase(q,q,pageable);if(category!=null&&!category.isBlank())return new PageImpl<>(result.stream().filter(p->category.equalsIgnoreCase(p.getCategory())).map(this::response).toList(),pageable,result.getTotalElements());return result.map(this::response);}
 @Transactional(readOnly=true) public ProductResponse one(Long id){return response(entity(id));} @Transactional(readOnly=true) public List<ProductResponse> mine(String name){return products.findBySellerOrderByIdDesc(users.find(name)).stream().map(this::response).toList();}
 @Transactional public ProductResponse create(ProductRequest r,String name){User seller=users.find(name);if(seller.getRole()!=Role.SELLER)throw new IllegalArgumentException("Seller account required");Product p=new Product();apply(p,r);p.setSeller(seller);return response(products.save(p));}
 @Transactional public ProductResponse update(Long id,ProductRequest r,String name){Product p=owned(id,name);apply(p,r);return response(products.save(p));}
 @Transactional public void delete(Long id,String name){Product p=owned(id,name);carts.deleteByProduct(p);products.delete(p);}
 private Product owned(Long id,String n){Product p=entity(id);if(!p.getSeller().getUsername().equals(n))throw new IllegalArgumentException("You can only manage your own products");return p;}
 private Product entity(Long id){return products.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found"));}
 private void apply(Product p,ProductRequest r){p.setBrand(r.brand());p.setModel(r.model());p.setCategory(r.category());p.setDescription(r.description());p.setPrice(r.price());p.setRating(r.rating());p.setImageUrl(r.imageUrl());}
 public ProductResponse response(Product p){return new ProductResponse(p.getId(),p.getBrand(),p.getModel(),p.getCategory(),p.getDescription(),p.getPrice(),p.getRating(),p.getImageUrl(),p.getSeller().getUsername());}
 public Product raw(Long id){return entity(id);}
}
