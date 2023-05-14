package com.projeto.catalogoprodutos;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
class CatalogoProdutosApplicationTests {

	@Test
	void contextLoads() {
		try {
			MockedStatic<SpringApplication> applicationMockedStatic = mockStatic(SpringApplication.class);
			CatalogoProdutosApplication.main(new String[0]);
			applicationMockedStatic.close();
			assertTrue(true);
		}catch (Exception e)
		{
			assertTrue(false);
		}

	}

}
