package com.ecommerce.service;

import com.ecommerce.dto.ApiDtos.ProductRequest;
import com.ecommerce.entity.*;
import com.ecommerce.repository.*;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Test void anotherSellerCannotUpdateOrDeleteProduct() {
        User owner=new User(); owner.setUsername("owner"); owner.setRole(Role.SELLER);
        Product product=new Product(); product.setSeller(owner);
        ProductRepository products=mock(ProductRepository.class); when(products.findById(1L)).thenReturn(Optional.of(product));
        ProductService service=new ProductService(products,mock(CartItemRepository.class),mock(UserService.class));
        ProductRequest update=new ProductRequest("A","B","C","Description",10,3,"/uploads/a.jpg");
        assertThrows(IllegalArgumentException.class,()->service.update(1L,update,"attacker"));
        assertThrows(IllegalArgumentException.class,()->service.delete(1L,"attacker"));
        verify(products,never()).delete(any());
    }
}
