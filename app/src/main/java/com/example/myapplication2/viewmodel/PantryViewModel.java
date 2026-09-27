package com.example.myapplication2.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication2.db.IngredientRepository;
import com.example.myapplication2.model.Ingredient;

import java.util.List;

public class PantryViewModel extends AndroidViewModel {
    private IngredientRepository repository;
    private LiveData<List<Ingredient>> allIngredients;

    public PantryViewModel(Application application) {
        super(application);
        repository = IngredientRepository.getInstance(application);
        allIngredients = repository.getAllIngredients();
    }

    public LiveData<List<Ingredient>> getAllIngredients() { return allIngredients; }

    public void insert(Ingredient ingredient) { repository.insert(ingredient); }
    public void update(Ingredient ingredient) { repository.update(ingredient); }
    public void delete(Ingredient ingredient) { repository.delete(ingredient); }
}
