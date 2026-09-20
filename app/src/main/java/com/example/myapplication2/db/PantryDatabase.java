package com.example.myapplication2.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication2.dao.PantryDao;
import com.example.myapplication2.dao.RecipeDao;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.Recipe;
import com.example.myapplication2.model.RecipeIngredient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Ingredient.class, Recipe.class, RecipeIngredient.class}, version = 2, exportSchema = false)
public abstract class PantryDatabase extends RoomDatabase {
    public abstract PantryDao pantryDao();
    public abstract RecipeDao recipeDao();

    private static volatile PantryDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PantryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PantryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PantryDatabase.class, "pantry_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration(true)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                RecipeDao dao = INSTANCE.recipeDao();
                
                // Seed 20 Recipes
                seedRecipes(dao);
            });
        }
    };

    private static void seedRecipes(RecipeDao dao) {
        // 1. Scrambled Eggs
        long id = dao.insertRecipe(new Recipe("Scrambled Eggs", "1. Beat eggs with salt. 2. Heat pan. 3. Cook until set."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Eggs", 2, "pcs"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Salt", 0.5, "tsp"));

        // 2. Pasta Aglio e Olio
        id = dao.insertRecipe(new Recipe("Pasta Aglio e Olio", "1. Boil pasta. 2. Sauté garlic in oil. 3. Mix with pasta."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Pasta", 200, "g"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Garlic", 3, "cloves"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Olive Oil", 2, "tbsp"));

        // 3. PB&J Sandwich
        id = dao.insertRecipe(new Recipe("PB&J Sandwich", "1. Spread PB on bread. 2. Spread Jelly. 3. Assemble."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Bread", 2, "slices"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Peanut Butter", 1, "tbsp"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Jelly", 1, "tbsp"));

        // 4. Tomato Soup
        id = dao.insertRecipe(new Recipe("Tomato Soup", "1. Sauté onions. 2. Add tomatoes and broth. 3. Simmer and blend."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Tomato", 4, "pcs"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Onion", 1, "pc"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Broth", 500, "ml"));

        // 5. Rice and Beans
        id = dao.insertRecipe(new Recipe("Rice and Beans", "1. Cook rice. 2. Heat beans. 3. Mix and season."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Rice", 1, "cup"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Beans", 1, "can"));

        // 6. Pancakes
        id = dao.insertRecipe(new Recipe("Pancakes", "1. Mix flour, eggs, milk. 2. Cook on griddle."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Flour", 1, "cup"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Eggs", 1, "pc"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Milk", 1, "cup"));

        // 7. Grilled Cheese
        id = dao.insertRecipe(new Recipe("Grilled Cheese", "1. Butter bread. 2. Add cheese. 3. Grill until golden."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Bread", 2, "slices"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Cheese", 2, "slices"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Butter", 1, "tbsp"));

        // 8. Omelette
        id = dao.insertRecipe(new Recipe("Omelette", "1. Beat eggs. 2. Pour into pan. 3. Fold with cheese."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Eggs", 3, "pcs"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Cheese", 1, "slice"));

        // 9. Baked Potato
        id = dao.insertRecipe(new Recipe("Baked Potato", "1. Poke potato. 2. Bake at 200C for 1 hour."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Potato", 1, "pc"));

        // 10. Simple Salad
        id = dao.insertRecipe(new Recipe("Simple Salad", "1. Chop lettuce and tomato. 2. Toss with dressing."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Lettuce", 1, "head"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Tomato", 1, "pc"));

        // 11. Boiled Eggs
        id = dao.insertRecipe(new Recipe("Boiled Eggs", "1. Place eggs in water. 2. Boil for 10 mins."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Eggs", 2, "pcs"));

        // 12. Mashed Potatoes
        id = dao.insertRecipe(new Recipe("Mashed Potatoes", "1. Boil potatoes. 2. Mash with butter and milk."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Potato", 3, "pcs"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Butter", 2, "tbsp"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Milk", 0.25, "cup"));

        // 13. Garlic Bread
        id = dao.insertRecipe(new Recipe("Garlic Bread", "1. Mix butter and garlic. 2. Spread on bread. 3. Toast."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Bread", 1, "loaf"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Butter", 2, "tbsp"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Garlic", 2, "cloves"));

        // 14. Tuna Salad
        id = dao.insertRecipe(new Recipe("Tuna Salad", "1. Mix tuna with mayo."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Tuna", 1, "can"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Mayo", 2, "tbsp"));

        // 15. Quesadilla
        id = dao.insertRecipe(new Recipe("Quesadilla", "1. Place cheese on tortilla. 2. Fold and grill."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Tortilla", 1, "pc"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Cheese", 0.5, "cup"));

        // 16. Fruit Salad
        id = dao.insertRecipe(new Recipe("Fruit Salad", "1. Chop apple and banana. 2. Mix."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Apple", 1, "pc"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Banana", 1, "pc"));

        // 17. Hot Cocoa
        id = dao.insertRecipe(new Recipe("Hot Cocoa", "1. Heat milk. 2. Mix with cocoa powder and sugar."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Milk", 1, "cup"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Cocoa Powder", 2, "tsp"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Sugar", 1, "tsp"));

        // 18. Oatmeal
        id = dao.insertRecipe(new Recipe("Oatmeal", "1. Boil oats in water or milk."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Oats", 0.5, "cup"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Water", 1, "cup"));

        // 19. Avocado Toast
        id = dao.insertRecipe(new Recipe("Avocado Toast", "1. Toast bread. 2. Mash avocado on top."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Bread", 1, "slice"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Avocado", 1, "pc"));

        // 20. Tea
        id = dao.insertRecipe(new Recipe("Tea", "1. Boil water. 2. Steep tea bag."));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Water", 1, "cup"));
        dao.insertRecipeIngredient(new RecipeIngredient((int)id, "Tea Bag", 1, "pc"));
    }
}
