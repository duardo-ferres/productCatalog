package com.projeto.catalogoprodutos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProductTest implements Serializable {
    Product product;
    @BeforeEach
    void initialize()
    {
        product = new Product();
    }
    @Test
    void idTest(){
        product.setId(90);
        assertEquals(90, product.getId());
    }
    @Test
    void nameTest(){
        product.setName("name");
        assertEquals("name", product.getName());
    }
    @Test
    void descriptionTest(){
        product.setDescription("description");
        assertEquals("description", product.getDescription());
    }
    @Test
    void priceTest(){
        product.setPrice(BigDecimal.valueOf(10.01));
        assertEquals(product.getPrice(), BigDecimal.valueOf(10.01));
    }
}
