package com.Galvanize.CrudRecipe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
class CrudRecipeApplicationTests {

	@Test
	void contextLoads() {
		String[] args = new String[0];
		CrudRecipeApplication.main(args);
	}

}
