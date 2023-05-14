package com.projeto.catalogoprodutos.service;

import com.projeto.catalogoprodutos.model.Product;
import com.projeto.catalogoprodutos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    ProductService productService;

    @BeforeEach
    void initialize()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllProducts()
    {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        doReturn(productList).when(productRepository).getAllProducts();
        assertSame(productService.getAllProducts(), productList);
    }

    @Test
    void getProductsById()
    {
        Product product = new Product();
        doReturn(product).when(productRepository).getProductsById(anyInt());
        assertSame(productService.getProductsById(10), product);
    }

    @Test
    void searchProducts()
    {

        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        doReturn(productList).when(productRepository).searchProducts(any(BigDecimal.class), any(BigDecimal.class), anyString());
        assertSame(productService.searchProducts("string", BigDecimal.valueOf(10), BigDecimal.valueOf(10)), productList);
    }

    @Test
    void searchProducts_01()
    {

        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        doReturn(productList).when(productRepository).searchProducts(any(BigDecimal.class), any(BigDecimal.class), anyString());
        assertSame(productService.searchProducts(null, BigDecimal.valueOf(10), BigDecimal.valueOf(10)), productList);
    }

    @Test
    void searchProducts_02()
    {

        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        doReturn(productList).when(productRepository).searchProducts(any(BigDecimal.class), any(BigDecimal.class), anyString());
        assertSame(productService.searchProducts("string", null, BigDecimal.valueOf(10)), productList);
    }

    @Test
    void searchProducts_03()
    {

        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        doReturn(productList).when(productRepository).searchProducts(any(BigDecimal.class), any(BigDecimal.class), anyString());
        assertSame(productService.searchProducts("string", BigDecimal.valueOf(10), null), productList);
    }

    @Test
    void insertProduct()
    {
        Product product = new Product();
        product.setName("name");
        product.setDescription("desc");
        product.setId(0);
        product.setPrice(BigDecimal.valueOf(12.50));

        doReturn(1).when(productRepository).insertProduct(any(Product.class));
        doReturn(1).when(productRepository).getLastProductId();


        Product productIn = new Product();
        productIn.setName("name");
        productIn.setDescription("desc");
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(12.50));

        Product received = productService.insertProduct(productIn);

        assertTrue(received.getId() > 0);
    }

    @Test
    void insertProduct_01()
    {
        Product productIn = new Product();
        productIn.setName(null);
        productIn.setDescription("desc");
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(12.50));

        productService.insertProduct(productIn);

        productIn.setName("");

        Product received = productService.insertProduct(productIn);
        assertEquals(received.getId(), -2);
    }

    @Test
    void insertProduct_02()
    {
        Product productIn = new Product();
        productIn.setName("name");
        productIn.setDescription(null);
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(12.50));

        productService.insertProduct(productIn);

        productIn.setDescription("");

        Product received = productService.insertProduct(productIn);
        assertEquals(received.getId() ,-3);
    }

    @Test
    void insertProduct_03()
    {
        Product productIn = new Product();
        productIn.setName("name");
        productIn.setDescription("desc");
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(-12.50));

        productService.insertProduct(productIn);

        productIn.setPrice(null);

        Product received = productService.insertProduct(productIn);
        assertEquals(received.getId(), -1);
    }

    @Test
    void alterProduct()
    {
        Product product = new Product();
        product.setName("name");
        product.setDescription("desc");
        product.setId(0);
        product.setPrice(BigDecimal.valueOf(12.50));

        doReturn(1).when(productRepository).alterProduct(anyInt(), any(Product.class));
        doReturn(product).when(productRepository).getProductsById(anyInt());


        Product productIn = new Product();
        productIn.setName("name");
        productIn.setDescription("desc");
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(12.50));

        Product received = productService.alterProduct(1, productIn);

        assertTrue(received.getId() > 0);
    }

    @Test
    void alterProduct_01()
    {
        Product product = new Product();
        product.setName("name");
        product.setDescription("desc");
        product.setId(0);
        product.setPrice(BigDecimal.valueOf(12.50));

        doReturn(product).when(productRepository).getProductsById(anyInt());

        Product productIn = new Product();
        productIn.setName(null);
        productIn.setDescription("desc");
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(12.50));

        productService.alterProduct(1, productIn);

        productIn.setName("");

        Product received = productService.alterProduct(1, productIn);

        assertEquals(received.getId(), -2);
    }

    @Test
    void alterProduct_02()
    {
        Product product = new Product();
        product.setName("name");
        product.setDescription("desc");
        product.setId(0);
        product.setPrice(BigDecimal.valueOf(12.50));

        doReturn(product).when(productRepository).getProductsById(anyInt());

        Product productIn = new Product();
        productIn.setName("name");
        productIn.setDescription(null);
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(12.50));

        productService.alterProduct(1, productIn);

        productIn.setDescription("");

        Product received = productService.alterProduct(1, productIn);

        assertEquals(received.getId(), -3);
    }

    @Test
    void alterProduct_03()
    {
        Product product = new Product();
        product.setName("name");
        product.setDescription("desc");
        product.setId(0);
        product.setPrice(BigDecimal.valueOf(12.50));

        doReturn(product).when(productRepository).getProductsById(anyInt());

        Product productIn = new Product();
        productIn.setName("name");
        productIn.setDescription("desc");
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(-12.50));

        productService.alterProduct(1, productIn);

        productIn.setPrice(null);

        Product received = productService.alterProduct(1, productIn);

        assertEquals(received.getId(), -1);
    }

    @Test
    void alterProduct_04()
    {
        Product product = new Product();
        product.setName("name");
        product.setDescription("desc");
        product.setId(0);
        product.setPrice(BigDecimal.valueOf(12.50));

        doReturn(null).when(productRepository).getProductsById(anyInt());

        Product productIn = new Product();
        productIn.setName("name");
        productIn.setDescription("desc");
        productIn.setId(0);
        productIn.setPrice(BigDecimal.valueOf(-12.50));

        Product received = productService.alterProduct(1, productIn);

        assertEquals(received.getId(), -4);
    }

    @Test
    void deleteProductById()
    {
        doReturn(1).when(productRepository).deleteProductsById(anyInt());
        doReturn(new Product()).when(productRepository).getProductsById(anyInt());
        int status = productService.deleteProductById(1);
        assertEquals(1, status);
    }

    @Test
    void deleteProductById_01()
    {
        doReturn(null).when(productRepository).getProductsById(anyInt());
        int status = productService.deleteProductById(1);
        assertEquals(status, -1);
    }
}
