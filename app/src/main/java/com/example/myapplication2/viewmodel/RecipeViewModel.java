package com.example.myapplication2.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication2.db.IngredientRepository;
import com.example.myapplication2.db.PantryDatabase;
import com.example.myapplication2.logic.MatchingEngine;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private IngredientRepository repository;
    private MutableLiveData<List<Recipe>> suggestedRecipes = new MutableLiveData<>();

    public RecipeViewModel(Application application) {
        super(application);
        repository = IngredientRepository.getInstance(application);
    }

    public LiveData<List<Recipe>> getSuggestedRecipes() { return suggestedRecipes; }

    public void updateSuggestions() {
        PantryDatabase.databaseWriteExecutor.execute(() -> {
            List<Ingredient> pantry = repository.getAllIngredientsSync();
            List<Recipe> allRecipes = repository.getAllRecipesSync();
            List<Recipe> matches = new ArrayList<>();

            for (Recipe recipe : allRecipes) {
                List<RecipeIngredient> required = repository.getIngredientsForRecipe(recipe.getId());
                if (MatchingEngine.canMakeRecipe(required, pantry)) {
                    matches.add(recipe);
                }
            }
            suggestedRecipes.postValue(matches);
        });
    }
    
    public Recipe getRecipeById(int id) {
        return repository.getRecipeById(id);
    }
    
    public List<RecipeIngredient> getIngredientsForRecipe(int id) {
        return repository.getIngredientsForRecipe(id);
    }
}
