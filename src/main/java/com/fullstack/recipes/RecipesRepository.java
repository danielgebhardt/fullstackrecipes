package com.fullstack.recipes;

import org.springframework.data.repository.CrudRepository;

public interface RecipesRepository extends CrudRepository<Recipe, Long> {
}
