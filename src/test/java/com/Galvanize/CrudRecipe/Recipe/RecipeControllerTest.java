package com.Galvanize.CrudRecipe.Recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.common.util.StringHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class RecipeControllerTest {

    Recipe recipe1;
    Recipe recipe2;

    @Autowired
    MockMvc mvc;

    @Autowired
    RecipeRepository repository;

    @BeforeEach
    void setup() {
        recipe1 = new Recipe();
        recipe1.setDateCreated(LocalDate.of(2020, 02, 20));
        recipe1.setCalories(4000);
        recipe1.setTitle("Fatboy Slims");
        recipe1.setDescription("Food + Calories");
        recipe1.setInstructions("Make food and eat");


        recipe2 = new Recipe();
        recipe2.setDateCreated(LocalDate.of(2022, 10, 8));
        recipe2.setCalories(100);
        recipe2.setTitle("Slimboy Slims");
        recipe2.setDescription("Food - Calories + Hungry");
        recipe2.setInstructions("Make food and throw away, eat from trash later");

    }

    @Test
    @Transactional
    @Rollback
    public void testGetNoPath() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/recipe")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetNone() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/recipe")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotHaveJsonPath());

    }

    @Test
    @Transactional
    @Rollback
    public void testGetOne() throws Exception {

        recipe1.setId(recipe1.getId());
        this.repository.save(recipe1);

        MockHttpServletRequestBuilder requestBuilder = get("/recipe")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dateCreated").value("2020-02-20"))
                .andExpect(jsonPath("$[0].calories").value(4000))
                .andExpect(jsonPath("$[0].title").value("Fatboy Slims"))
                .andExpect(jsonPath("$[0].description").value("Food + Calories"))
                .andExpect(jsonPath("$[0].instructions").value("Make food and eat"))
                .andExpect(jsonPath("$[0].id").value(recipe1.getId()));

    }



    @Test
    @Transactional
    @Rollback
    public void testGetMultiple() throws Exception {

        this.repository.save(recipe1);
        this.repository.save(recipe2);

        MockHttpServletRequestBuilder requestBuilder = get("/recipe")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dateCreated").value("2020-02-20"))
                .andExpect(jsonPath("$[0].calories").value(4000))
                .andExpect(jsonPath("$[0].title").value("Fatboy Slims"))
                .andExpect(jsonPath("$[0].description").value("Food + Calories"))
                .andExpect(jsonPath("$[0].instructions").value("Make food and eat"))
                .andExpect(jsonPath("$[0].id").value(recipe1.getId()))

                .andExpect(jsonPath("$[1].dateCreated").value("2022-10-08"))
                .andExpect(jsonPath("$[1].calories").value(100))
                .andExpect(jsonPath("$[1].title").value("Slimboy Slims"))
                .andExpect(jsonPath("$[1].description").value("Food - Calories + Hungry"))
                .andExpect(jsonPath("$[1].instructions").value("Make food and throw away, eat from trash later"))
                .andExpect(jsonPath("$[1].id").value(recipe2.getId()));
    }


    @Test
    @Transactional
    @Rollback
    public void testPost() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> myRecipeHash = new HashMap<>();
        myRecipeHash.put("dateCreated", "2020-02-20");
        myRecipeHash.put("calories", "4000");
        myRecipeHash.put("title", "Fatboy Slims");
        myRecipeHash.put("description", "Food + Calories");
        myRecipeHash.put("instructions", "Make food and eat");

        String json = mapper.writeValueAsString(myRecipeHash);

        MockHttpServletRequestBuilder requestBuilder = post("/recipe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateCreated").value("2020-02-20"))
                .andExpect(jsonPath("$.calories").value(4000))
                .andExpect(jsonPath("$.title").value("Fatboy Slims"))
                .andExpect(jsonPath("$.description").value("Food + Calories"))
                .andExpect(jsonPath("$.instructions").value("Make food and eat"))
                .andExpect(jsonPath("$.id").value(1));

        this.mvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dateCreated").value("2020-02-20"))
                .andExpect(jsonPath("$[0].calories").value(4000))
                .andExpect(jsonPath("$[0].title").value("Fatboy Slims"))
                .andExpect(jsonPath("$[0].description").value("Food + Calories"))
                .andExpect(jsonPath("$[0].instructions").value("Make food and eat"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @Transactional
    @Rollback
    public void testPatchOne() throws Exception {
        this.repository.save(recipe2);
        String json = "{\"calories\":\"500\"}";

        MockHttpServletRequestBuilder requestBuilder = patch(String.format("/recipe/%d", recipe2.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateCreated").value("2022-10-08"))
                .andExpect(jsonPath("$.calories").value(500))
                .andExpect(jsonPath("$.title").value("Slimboy Slims"))
                .andExpect(jsonPath("$.description").value("Food - Calories + Hungry"))
                .andExpect(jsonPath("$.instructions").value("Make food and throw away, eat from trash later"))
                .andExpect(jsonPath("$.id").value(recipe2.getId()));

        this.mvc.perform(get(String.format("/recipe/%d", recipe2.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateCreated").value("2022-10-08"))
                .andExpect(jsonPath("$.calories").value(500))
                .andExpect(jsonPath("$.title").value("Slimboy Slims"))
                .andExpect(jsonPath("$.description").value("Food - Calories + Hungry"))
                .andExpect(jsonPath("$.instructions").value("Make food and throw away, eat from trash later"))
                .andExpect(jsonPath("$.id").value(recipe2.getId()));

    }

    @Test
    @Transactional
    @Rollback
    public void testPatchMultiple() throws Exception {

        this.repository.save(recipe1);

        String json = """
                {
                  "description": "Food + Calories + Cheese",
                  "dateCreated": "2022-10-25",
                  "title": "Fatboyyy Slims",
                  "calories": 5000,
                  "instructions":"Do it or die"
                }
                """;

        MockHttpServletRequestBuilder requestBuilder = patch(String.format("/recipe/%d", recipe1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateCreated").value("2022-10-25"))
                .andExpect(jsonPath("$.calories").value(5000))
                .andExpect(jsonPath("$.title").value("Fatboyyy Slims"))
                .andExpect(jsonPath("$.description").value("Food + Calories + Cheese"))
                .andExpect(jsonPath("$.instructions").value("Do it or die"))
                .andExpect(jsonPath("$.id").value(recipe1.getId()));

        this.mvc.perform(get(String.format("/recipe/%d", recipe1.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateCreated").value("2022-10-25"))
                .andExpect(jsonPath("$.calories").value(5000))
                .andExpect(jsonPath("$.title").value("Fatboyyy Slims"))
                .andExpect(jsonPath("$.description").value("Food + Calories + Cheese"))
                .andExpect(jsonPath("$.instructions").value("Do it or die"))
                .andExpect(jsonPath("$.id").value(recipe1.getId()));
    }


    @Test
    @Transactional
    @Rollback
    public void testDelete() throws Exception {
            this.repository.save(recipe1);
            this.repository.save(recipe2);

            MockHttpServletRequestBuilder requestBuilder = delete(String.format("/recipe/%d",recipe2.getId()));

            this.mvc.perform(requestBuilder)
                    .andExpect(status().isNoContent());

            this.mvc.perform(get("/recipe/2"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$[1]").doesNotHaveJsonPath());
    }


    @Test
    @Transactional
    @Rollback
    public void testException() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/recipe/9")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

}
