package com.fullstack.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeApplicationTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    RecipesRepository recipeRepository;

    @Test
    @Transactional
    @Rollback
    void canCreateRecipe() throws Exception {
        Recipe testRecipe = new Recipe();
        testRecipe.title = "Guacamole";
        testRecipe.description = "add avacados and other stuff";
        testRecipe.calories = 50;

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(testRecipe);


        this.mvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title", is("Guacamole")))
                .andExpect(jsonPath("$.description", is("add avacados and other stuff")))
                .andExpect(jsonPath("$.calories", is(50)));
    }

    @Test
    @Transactional
    @Rollback
    public void canReadRecipe() throws Exception {
        Recipe testRecipe = new Recipe();
        testRecipe.title = "Guacamole";
        testRecipe.description = "add avacados and other stuff";
        testRecipe.calories = 50;

        Recipe newRecipe = recipeRepository.save(testRecipe);
        int newRecipeId = (int) newRecipe.id;

        this.mvc.perform(get("/recipes/" + newRecipeId))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.id", is(newRecipeId)))
                .andExpect(jsonPath("$.title", is("Guacamole")))
                .andExpect(jsonPath("$.description", is("add avacados and other stuff")))
                .andExpect(jsonPath("$.calories", is(50)));

    }

    @Test
    @Transactional
    @Rollback
    public void canUpdateRecipe() throws Exception {
        Recipe testRecipe = new Recipe();
        testRecipe.title = "Guacamole";
        testRecipe.description = "add avacados and other stuff";
        testRecipe.calories = 50;

        Recipe newRecipe = recipeRepository.save(testRecipe);
        int newRecipeId = (int) newRecipe.id;

        this.mvc.perform(patch("/recipes/" + newRecipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title":"New Improved Guac",
                                    "calories":80
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title", is("New Improved Guac")))
                .andExpect(jsonPath("$.description", is("add avacados and other stuff")))
                .andExpect(jsonPath("$.calories", is(80)));
    }

    @Test
    @Transactional
    @Rollback
    public void canDeleteRecipe() throws Exception {
        Recipe testRecipe = new Recipe();
        testRecipe.title = "Guacamole";
        testRecipe.description = "add avacados and other stuff";
        testRecipe.calories = 50;

        Recipe newRecipe = recipeRepository.save(testRecipe);
        int newRecipeId = (int) newRecipe.id;

        this.mvc.perform(delete("/recipes/" + newRecipeId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.format("Recipe %d deleted", newRecipeId)));

        this.mvc.perform(get("/recipes/" + newRecipeId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recipe Not Found"));
    }

    @Test
    @Transactional
    @Rollback
    public void canListRecipes() throws Exception {
        Recipe testRecipe = new Recipe();
        testRecipe.title = "Guacamole";
        testRecipe.description = "add avacados and other stuff";
        testRecipe.calories = 50;

        Recipe newRecipe = recipeRepository.save(testRecipe);
        int newRecipeId = (int) newRecipe.id;

        Recipe testRecipe2 = new Recipe();
        testRecipe2.title = "Grilled Cheese";
        testRecipe2.description = "bread, cheese";
        testRecipe2.calories = 870;

        Recipe newRecipe2 = recipeRepository.save(testRecipe2);
        int newRecipeId2 = (int) newRecipe2.id;

        this.mvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(newRecipeId)))
                .andExpect(jsonPath("$[0].title", is("Guacamole")))
                .andExpect(jsonPath("$[0].description", is("add avacados and other stuff")))
                .andExpect(jsonPath("$[0].calories", is(50)))
                .andExpect(jsonPath("$[1].id", is(newRecipeId2)))
                .andExpect(jsonPath("$[1].title", is("Grilled Cheese")))
                .andExpect(jsonPath("$[1].description", is("bread, cheese")))
                .andExpect(jsonPath("$[1].calories", is(870)));
    }

    @Test
    @Transactional
    @Rollback
    public void notFoundErrorReturnedWhenNotFound() throws Exception {

        this.mvc.perform(get("/recipes/456456"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recipe Not Found"));

        this.mvc.perform(patch("/recipes/456456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recipe Not Found, Unable to Update"));

        this.mvc.perform(delete("/recipes/456456"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Recipe Not Found, Unable to Delete"));

    }

    @Test
    @Transactional
    @Rollback
    public void handleBadFieldsPassedToPatch() throws Exception {
        Recipe testRecipe = new Recipe();
        testRecipe.title = "Guacamole";
        testRecipe.description = "add avacados and other stuff";
        testRecipe.calories = 50;

        Recipe newRecipe = recipeRepository.save(testRecipe);
        int newRecipeId = (int) newRecipe.id;

        this.mvc.perform(patch("/recipes/" + newRecipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "titfsdgdfsle":"New Improved Guac",
                                    "calories":80
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect Field Name, please fix"));

    }
}
