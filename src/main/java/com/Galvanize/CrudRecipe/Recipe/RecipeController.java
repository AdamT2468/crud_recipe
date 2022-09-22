package com.Galvanize.CrudRecipe.Recipe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class RecipeController {

    private final RecipeRepository repository;

    public RecipeController(RecipeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/recipe")
    public List<Recipe> getRecipe(){
        return this.repository.findAll();
    }

    @PostMapping("/recipe")
    public Recipe postRecipe(@RequestBody Recipe recipe){
        return this.repository.save(recipe);
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipeId(@PathVariable long id){
        return this.repository.findById(id).get();
    }

    @PatchMapping("/recipe/{id}")
    public Recipe postRecipe(@RequestBody Map<String,String> recipeMap, @PathVariable long id){
        Recipe oldRecipe = this.repository.findById(id).get();

        recipeMap.forEach(
                (key,value)->{
                    if (key.equals("description")){
                        oldRecipe.setDescription(value);
                    } else if (key.equals("instructions")){
                        oldRecipe.setInstructions(value);
                    } else if (key.equals("title")){
                        oldRecipe.setTitle(value);
                    } else if (key.equals("calories")){
                        oldRecipe.setCalories(Integer.valueOf(value));
                    } else if (key.equals("dateCreated")){
                        oldRecipe.setDateCreated(LocalDate.parse(value));
                    }
                }
        );
        return this.repository.save(oldRecipe);
    }



    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<Object> deleteRecipe (@PathVariable long id){
        this.repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String exceptionHandler(){
        return "Nope";
    }
}
