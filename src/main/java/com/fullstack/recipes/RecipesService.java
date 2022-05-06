package com.fullstack.recipes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;

@Service
public class RecipesService {

    private final RecipesRepository recipesRepository;

    public RecipesService(RecipesRepository recipesRepository) {this.recipesRepository = recipesRepository;}

    public ResponseEntity<Object> createTheRecipe(Recipe recipe) {
        Recipe newRecipe = recipesRepository.save(recipe);

        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> readTheRecipe(long id) throws RecipeNotFoundException {
        Optional<Recipe> foundRecipe = recipesRepository.findById(id);

        if(foundRecipe.isEmpty()){
            throw new RecipeNotFoundException("Recipe Not Found");
        }

        return new ResponseEntity<>(foundRecipe.get(), HttpStatus.FOUND);
    }

    public ResponseEntity<Object> updateTheRecipe(long id, HashMap<String, Object> recipeMap) throws RecipeNotFoundException, NullPointerException {
        Optional<Recipe> foundRecipe = recipesRepository.findById(id);

        if(foundRecipe.isEmpty()){
            throw new RecipeNotFoundException("Recipe Not Found, Unable to Update");
        }

        recipeMap.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Recipe.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, foundRecipe.get(), value);
        });

        recipesRepository.save(foundRecipe.get());

        return new ResponseEntity<>(foundRecipe.get(), HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteTheRecipe(long id) throws RecipeNotFoundException {
        Optional<Recipe> foundRecipe = recipesRepository.findById(id);

        if(foundRecipe.isEmpty()){
            throw new RecipeNotFoundException("Recipe Not Found, Unable to Delete");
        }

        recipesRepository.deleteById(id);

        return new ResponseEntity<>(String.format("Recipe %d deleted", id), HttpStatus.OK);
    }

    public ResponseEntity<Object> listTheRecipes() {
        return new ResponseEntity<>(recipesRepository.findAll(), HttpStatus.OK);
    }
}
