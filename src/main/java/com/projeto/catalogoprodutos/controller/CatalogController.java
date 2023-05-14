package com.projeto.catalogoprodutos.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.catalogoprodutos.model.Product;
import com.projeto.catalogoprodutos.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiOperation("Products Catalog")
@Controller
public class CatalogController {
    static final String CONST_STATUS_CODE = "status_code";
    static final String CONST_DESCRIPTION = "description";

    @Autowired
    ProductService productService;

    @ApiOperation(value = "Exposes the health and timestamp of application", notes = "the timestamp is expressed in unix timestamp format")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success to get application health", response = Map.class)
    })
    @GetMapping(value = "/")
    public ResponseEntity<String> health() throws JsonProcessingException {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "OK");
        health.put("timestamp", new Date());
        return new ResponseEntity<>(new ObjectMapper().writeValueAsString(health), HttpStatus.valueOf(200));
    }

    /**
     *
     * @param id product id
     * @return selected product
     * @throws JsonProcessingException
     */

    @ApiOperation(value = "Get product by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success to get product by id ", response = Product.class),
            @ApiResponse(code = 404, message = "Product id not found on database")
    })
    @GetMapping(value = "/products/{id}")
    public ResponseEntity<String> getProductById(@ApiParam(name = "id", value = "Product ID", example = "1") @PathVariable("id") int id) throws JsonProcessingException {
        //constroi mensagem de erro
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(CONST_STATUS_CODE, 404);
        errorResponse.put(CONST_DESCRIPTION, "error to get product, not found");

        //busca produto
        Product product = productService.getProductsById(id);

        if(product == null)
        {
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(errorResponse), HttpStatus.valueOf(404));
        }
        else
        {
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(product), HttpStatus.valueOf(200));
        }
    }

    /**
     *
     * @return list of selected product
     * @throws JsonProcessingException
     */

    @ApiOperation(value = "Get all products on database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success to get products", response = List.class)
    })

    @GetMapping(value = "/products")
    public ResponseEntity<String> getAllProducts() throws JsonProcessingException {
        return new ResponseEntity<>(new ObjectMapper().writeValueAsString(productService.getAllProducts()), HttpStatus.valueOf(200));
    }

    /**
     *
     * @param q needle to find on name and description
     * @param minPrice min price filter
     * @param maxPrice max price filter
     * @return list of selected products
     * @throws JsonProcessingException
     */

    @ApiOperation(value = "Search products by name, description or price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success to find products", response = List.class)
    })
    @GetMapping(value = "/products/search")
    public ResponseEntity<String> searchProduct(@ApiParam(name = "q", example = "product", value = "Needle to find on name or description") @RequestParam(value="q", required=false) String q,
                                                @ApiParam(name = "minPrice", example = "10.00", value = "Minimum price filter") @RequestParam(value="minPrice", required=false) BigDecimal minPrice,
                                                @ApiParam(name = "maxPrice", example = "50.00", value = "Maximum price filter") @RequestParam(value="maxPrice", required=false) BigDecimal maxPrice
                                                 ) throws JsonProcessingException {
        return new ResponseEntity<>(new ObjectMapper().writeValueAsString(productService.searchProducts(q,minPrice,maxPrice)), HttpStatus.valueOf(200));
    }

    /**
     *
     * @param body product request body
     * @return added product
     * @throws JsonProcessingException
     */

    @ApiOperation(value = "Insert product on database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success to insert product on database", response = Product.class),
            @ApiResponse(code = 400, message = "Product validation error")
    })
    @PostMapping("products")
    public ResponseEntity<String> insertProduct(@ApiParam(name = "Product", value = "Product in json format") @RequestBody Product body) throws JsonProcessingException {
        //constroi mensagem de erro
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(CONST_STATUS_CODE, 400);
        errorResponse.put(CONST_DESCRIPTION, "error to add product to catalog");

        //insere produto
        Product product = productService.insertProduct(body);

        switch (product.getId()){
            case -1:
                errorResponse.put(CONST_DESCRIPTION, "error to add product to catalog, price is negative or null");
                break;
            case -2:
                errorResponse.put(CONST_DESCRIPTION, "error to add product to catalog, missing product name");
                break;
            case -3:
                errorResponse.put(CONST_DESCRIPTION, "error to add product to catalog, missing product description");
                break;
            default:
                errorResponse.put(CONST_DESCRIPTION, "error to add product to catalog");
        }
        //processa as mensagens de erro
        if(product.getId() < 1)
        {
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(errorResponse), HttpStatus.valueOf((int) errorResponse.get(CONST_STATUS_CODE)));
        }

        return new ResponseEntity<>(new ObjectMapper().writeValueAsString(product), HttpStatus.valueOf(201));

    }

    /**
     *
     * @param body product request body
     * @param id id of product to edit
     * @return altered product
     * @throws JsonProcessingException
     */

    @ApiOperation(value = "Alter product on database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success to alter product on database", response = Product.class),
            @ApiResponse(code = 400, message = "Product validation error"),
            @ApiResponse(code = 404, message = "Product not found on database")
    })
    @PutMapping("products/{id}")
    public ResponseEntity<String> alterProduct(@ApiParam(name = "Product", value = "Product in json format") @RequestBody Product body, @ApiParam(name = "id", example = "1", value = "Product ID to alter on database") @PathVariable("id") int id) throws JsonProcessingException {
        //constroi mensagem de erro
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(CONST_STATUS_CODE, 400);
        errorResponse.put(CONST_DESCRIPTION, "error to alter product to catalog");

        //Altera produto
        Product product = productService.alterProduct(id, body);

        //constroi mensagem de erro
        switch (product.getId()){
            case -1:
                errorResponse.put(CONST_DESCRIPTION, "error to alter product on catalog, price is negative or null");
                break;
            case -2:
                errorResponse.put(CONST_DESCRIPTION, "error to alter product on catalog, missing product name");
                break;
            case -3:
                errorResponse.put(CONST_DESCRIPTION, "error to alter product on catalog, missing product description");
                break;
            case -4:
                errorResponse.put(CONST_DESCRIPTION, "error to alter product on catalog, product does not exist");
                errorResponse.put(CONST_STATUS_CODE, 404);
                break;
            default:
                errorResponse.put(CONST_DESCRIPTION, "error to alter product on catalog");
        }

        //processa as mensagens de erro
        if(product.getId() < 1)
        {
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(errorResponse), HttpStatus.valueOf((int) errorResponse.get(CONST_STATUS_CODE)));
        }

        return new ResponseEntity<>(new ObjectMapper().writeValueAsString(product), HttpStatus.valueOf(201));
    }

    /**
     *
     * @param id id of product
     * @return status of deletion
     * @throws JsonProcessingException
     */

    @ApiOperation(value = "Delete product from database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success to delete product on database"),
            @ApiResponse(code = 404, message = "Product not found on database")
    })
    @DeleteMapping("products/{id}")
    public ResponseEntity<String> deleteProduct(@ApiParam(name = "id", example = "1", value = "Product ID to delete") @PathVariable int id) throws JsonProcessingException {
        //Constroi mensagem de erro
        Map<String, Object> response = new HashMap<>();

        int resp = productService.deleteProductById(id);
        if(resp <= 0){
            response.put(CONST_STATUS_CODE, 404);
            response.put(CONST_DESCRIPTION, "error to delete, product not found");
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(response), HttpStatus.valueOf(404));
        }
        else {
            response.put(CONST_STATUS_CODE, 200);
            response.put(CONST_DESCRIPTION, "success");
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(response), HttpStatus.valueOf(200));
        }
    }
}
