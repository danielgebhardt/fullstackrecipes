package com.fullstack.recipes;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/recipes")
public class RecipesController {

    private final RecipesService recipesService;

    public RecipesController(RecipesService recipesService) {this.recipesService = recipesService;}

    @PostMapping
    public ResponseEntity<Object> canCreateRecipe(@RequestBody Recipe recipe) {
        return recipesService.createTheRecipe(recipe);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> canReadRecipe(@PathVariable long id) throws RecipeNotFoundException {
        return recipesService.readTheRecipe(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> canUpdateRecipe(@PathVariable long id, @RequestBody HashMap<String, Object> recipeMap) throws RecipeNotFoundException {
        return recipesService.updateTheRecipe(id, recipeMap);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> canDeleteRecipe(@PathVariable long id) throws RecipeNotFoundException {
        return recipesService.deleteTheRecipe(id);
    }

    @GetMapping()
    public ResponseEntity<Object> canListRecipe() {
        return recipesService.listTheRecipes();
    }
}
