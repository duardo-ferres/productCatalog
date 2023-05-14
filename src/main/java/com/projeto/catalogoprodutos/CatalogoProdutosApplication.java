package com.projeto.catalogoprodutos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@AutoConfiguration
@EnableJpaRepositories
public class CatalogoProdutosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogoProdutosApplication.class, args);
	}

}
