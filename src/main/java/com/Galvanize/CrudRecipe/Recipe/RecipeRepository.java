package com.Galvanize.CrudRecipe.Recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCalories (Integer calories);

    List<Recipe> findByCaloriesBetween (@Param("calories1") Integer caloriesLow, @Param("calories2") Integer caloriesHigh);

    List<Recipe> findByCaloriesLessThanEqual(Integer calories);

    List<Recipe> findByCaloriesOrderByCaloriesAsc();
}
