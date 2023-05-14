package com.projeto.catalogoprodutos.repository;

import com.projeto.catalogoprodutos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Transactional
    @Query(value = "SELECT `id`, `name`, `description`, `price` FROM `product` WHERE `id` = :id ", nativeQuery = true)
    Product getProductsById(@Param("id") int id);

    @Query(value = "SELECT `id`, `name`, `description`, `price` FROM `product`", nativeQuery = true)
    List<Product> getAllProducts();

    @Query(value = "SELECT `id`, `name`, `description`, `price` FROM product WHERE (price BETWEEN :minPrice AND :maxPrice) AND (`name` LIKE %:needle% OR `description` LIKE %:needle%)", nativeQuery = true)
    List<Product> searchProducts(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, @Param("needle") String needle);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO `product` (`id`, `name`, `description`, `price`) VALUES (:#{#argProduct.id}, :#{#argProduct.name}, :#{#argProduct.description}, :#{#argProduct.price})", nativeQuery = true)
    int insertProduct(@Param("argProduct") Product product);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `product` SET `name` = :#{#argProduct.name}, `description` = :#{#argProduct.description}, `price` = :#{#argProduct.price} WHERE `id` = :id", nativeQuery = true)
    int alterProduct(@Param("id") int id, @Param("argProduct") Product product);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM product WHERE `id` = :id", nativeQuery = true)
    int deleteProductsById(@Param("id") int id);

    @Query(value = "SELECT MAX(`id`) FROM `product`", nativeQuery = true)
    Integer getLastProductId();
}
