package com.projeto.catalogoprodutos.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.projeto.catalogoprodutos.model.Product;
import com.projeto.catalogoprodutos.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CatalogControllerTest {
    @Mock
    ProductService productService;

    @InjectMocks
    CatalogController catalogController;

    @Test
    void health() throws JsonProcessingException {
        ResponseEntity<String> responseEntity = catalogController.health();
        assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(200));
    }

    @Test
    void getProductById() throws JsonProcessingException {
        //busca produto
        doReturn(new Product()).when(productService).getProductsById(anyInt());
        ResponseEntity<String> responseEntity = catalogController.getProductById(1);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(200));
    }

    @Test
    void getProductById_01() throws JsonProcessingException {

        //busca produto
        doReturn(null).when(productService).getProductsById(anyInt());
        ResponseEntity<String> responseEntity = catalogController.getProductById(1);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(404));
    }

    @Test
    void getAllProducts() throws JsonProcessingException {
        doReturn(new ArrayList<>()).when(productService).getAllProducts();
        ResponseEntity<String> responseEntity = catalogController.getAllProducts();
        assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(200));
    }

    @Test
    void searchProduct() throws JsonProcessingException {
        doReturn(new ArrayList<>()).when(productService).searchProducts(anyString(), any(BigDecimal.class), any(BigDecimal.class));
        ResponseEntity<String> responseEntity = catalogController.searchProduct("", BigDecimal.ONE, BigDecimal.ONE);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(200));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 0, -1, -2, -3})
    void insertProductSuccess(int id) throws JsonProcessingException {
        Product product = new Product();
        product.setId(id);
        product.setName("product");
        product.setDescription("description");
        product.setPrice(BigDecimal.valueOf(10.0));

        doReturn(product).when(productService).insertProduct(any(Product.class));

        ResponseEntity<String> responseEntity = catalogController.insertProduct(product);
        if(id == 1)
            assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(201));
        else
            assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(400));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 0, -1, -2, -3, -4}) // six numbers
    void alterProduct(int id) throws JsonProcessingException {
        Product product = new Product();
        product.setId(id);
        product.setName("product");
        product.setDescription("description");
        product.setPrice(BigDecimal.valueOf(10.0));

        doReturn(product).when(productService).alterProduct(anyInt(), any(Product.class));

        ResponseEntity<String> responseEntity = catalogController.alterProduct(product, 1);
        if(id == 1)
            assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(201));
        else if(id == -4)
            assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(404));
        else
            assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(400));
    }

    @Test
    void deleteProduct() throws JsonProcessingException {
        doReturn(1).when(productService).deleteProductById(anyInt());

        ResponseEntity<String> responseEntity = catalogController.deleteProduct(1);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(200));
    }

    @Test
    void deleteProduct_01() throws JsonProcessingException {
        doReturn(-1).when(productService).deleteProductById(anyInt());

        ResponseEntity<String> responseEntity = catalogController.deleteProduct(1);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.valueOf(404));
    }
}
