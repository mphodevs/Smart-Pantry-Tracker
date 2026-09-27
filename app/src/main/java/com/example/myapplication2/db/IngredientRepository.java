package com.example.myapplication2.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.myapplication2.dao.PantryDao;
import com.example.myapplication2.dao.RecipeDao;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;

import java.util.List;

public class IngredientRepository {
    private static volatile IngredientRepository INSTANCE;

    private final PantryDao pantryDao;
    private final RecipeDao recipeDao;
    private final LiveData<List<Ingredient>> allIngredients;
    private final LiveData<List<Recipe>> allRecipes;

    private IngredientRepository(Application application) {
        PantryDatabase db = PantryDatabase.getDatabase(application);
        pantryDao = db.pantryDao();
        recipeDao = db.recipeDao();
        allIngredients = pantryDao.getAllIngredients();
        allRecipes = recipeDao.getAllRecipes();
    }

    public static IngredientRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (IngredientRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new IngredientRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Ingredient>> getAllIngredients() { return allIngredients; }
    public LiveData<List<Recipe>> getAllRecipes() { return allRecipes; }

    public void insert(Ingredient ingredient) {
        PantryDatabase.databaseWriteExecutor.execute(() -> pantryDao.insert(ingredient));
    }

    public void update(Ingredient ingredient) {
        PantryDatabase.databaseWriteExecutor.execute(() -> pantryDao.update(ingredient));
    }

    public void delete(Ingredient ingredient) {
        PantryDatabase.databaseWriteExecutor.execute(() -> pantryDao.delete(ingredient));
    }

    public List<Ingredient> getAllIngredientsSync() { return pantryDao.getAllIngredientsSync(); }
    public List<Recipe> getAllRecipesSync() { return recipeDao.getAllRecipesSync(); }
    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) { return recipeDao.getIngredientsForRecipe(recipeId); }
    public Recipe getRecipeById(int recipeId) { return recipeDao.getRecipeById(recipeId); }
}
