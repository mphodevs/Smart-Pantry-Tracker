package com.example.myapplication2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import androidx.room.OnConflictStrategy;

import com.example.myapplication2.model.Ingredient;

import java.util.List;

@Dao
public interface PantryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);

    @Query("SELECT * FROM ingredients ORDER BY name ASC")
    LiveData<List<Ingredient>> getAllIngredients();
    
    @Query("SELECT * FROM ingredients")
    List<Ingredient> getAllIngredientsSync();
}
