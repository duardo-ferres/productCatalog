package com.projeto.catalogoprodutos.service;

import com.projeto.catalogoprodutos.model.Product;
import com.projeto.catalogoprodutos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    /**
     * get all product on database
     *
     * @return list of products
     */
    public List<Product> getAllProducts()
    {
        return productRepository.getAllProducts();
    }

    /**
     * get product on database by id
     *
     * @param id the id of product
     * @return product that corresponds the id
     */
    public Product getProductsById(int id)
    {
        return productRepository.getProductsById(id);
    }

    /**
     * search product on database
     *
     * @param filter needle as string to search on name or description
     * @param minPrice min price of products
     * @param maxPrice max price of products
     * @return list of filtered products
     */
    public List<Product> searchProducts(String filter, BigDecimal minPrice, BigDecimal maxPrice)
    {
        //Trabalha nulidades nos parametros
        if(minPrice == null)
        {
            minPrice = BigDecimal.valueOf(Double.MIN_VALUE);
        }
        if(maxPrice == null)
        {
            maxPrice = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        if(filter == null)
        {
            filter = "";
        }

        return productRepository.searchProducts(minPrice, maxPrice, filter);
    }

    /**
     * insert product to database returning inserted product
     *
     * @param product
     * @return added product
     *
     * the product id below 1 represents validation error
     *  0 = query execution error
     * -1 = price is negative or null
     * -2 = product name is missing
     * -3 = product dercription is missing
     */
    public Product insertProduct(Product product)
    {
        int productResponse = 0;

        //Verifica parametros e seta o erro no id do produto que por padrão não sera negativo em caso de sucesso
        if((product.getPrice() == null) || (product.getPrice().doubleValue() < 0))
        {
            productResponse = -1;
        }
        else if((product.getName() == null) || (product.getName().length() < 1))
        {
            productResponse = -2;
        }
        else if((product.getDescription()) == null || (product.getDescription().length() < 1)){
            productResponse = -3;
        }
        //caso validado insere o produto
        else{
            Integer productId = productRepository.getLastProductId();
            productId = productId == null ? 1 : productId + 1;
            product.setId(productId);
            productResponse = productRepository.insertProduct(product);
        }

        //verifica se a query inseriu algum registro
        if(productResponse != 1)
        {
            product.setId(productResponse);
        }
        return product;
    }

    /**
     * alter product on database returning altered product
     *
     * @param product
     * @return altered product
     *
     * the product id below 1 represents validation error
     *  0 = query execution error
     * -1 = price is negative or null
     * -2 = product name is missing
     * -3 = product dercription is missing
     * -4 = the product id does not exist
     */
    public Product alterProduct(int id, Product product)
    {
        //declara uma resposta para a query
        int productResponse = 0;

        //verifica a existencia do produto caso nao exista retorna erro
        Product currentProduct = productRepository.getProductsById(id);
        if(currentProduct == null)
        {
            productResponse = -4;
        }
        //Verifica parametros e seta o erro no id do produto que por padrão não sera negativo em caso de sucesso
        else if((product.getPrice() == null) || (product.getPrice().doubleValue() < 0))
        {
            productResponse = -1;
        }
        else if((product.getName() == null) || (product.getName().length() < 1))
        {
            productResponse = -2;
        }
        else if((product.getDescription()) == null || (product.getDescription().length() < 1)){
            productResponse = -3;
        }
        //caso validado edita o produto
        else{
            productResponse = productRepository.alterProduct(id, product);
            product.setId(id);
        }
        //verifica se a query inseriu algum registro
        if(productResponse != 1)
        {
            product.setId(productResponse);
        }
        return product;
    }

    /**
     * delete product from database by id
     *
     * @return status code
     *
     *  1 = success
     *  0 = query execution error
     * -1 = product does not exists
     */
    public int deleteProductById(int id)
    {
        //verifica a existencia do produto caso nao exista retorna erro
        Product currentProduct = productRepository.getProductsById(id);
        if(currentProduct == null)
        {
            return -1;
        }
        else
        {
            return productRepository.deleteProductsById(id);
        }
    }
}
