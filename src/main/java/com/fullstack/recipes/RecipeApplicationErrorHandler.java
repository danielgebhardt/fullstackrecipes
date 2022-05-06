package com.fullstack.recipes;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RecipeApplicationErrorHandler {
    @ExceptionHandler(value = RecipeNotFoundException.class)
    public ResponseEntity<String> handleRecipeNotFound(RecipeNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>("Incorrect Field Name, please fix", HttpStatus.BAD_REQUEST);
    }
}
