package com.example.myapplication2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.R;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.viewmodel.PantryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class PantryFragment extends Fragment {

    private PantryViewModel viewModel;
    private IngredientAdapter adapter;
    private TextView textEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_pantry);
        textEmpty = view.findViewById(R.id.text_empty_pantry);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_ingredient);

        adapter = new IngredientAdapter(new IngredientAdapter.IngredientDiff(), new IngredientAdapter.OnIngredientClickListener() {
            @Override
            public void onIngredientClick(Ingredient ingredient) {
                Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
                intent.putExtra("id", ingredient.getId());
                intent.putExtra("name", ingredient.getName());
                intent.putExtra("quantity", ingredient.getQuantity());
                intent.putExtra("unit", ingredient.getUnit());
                intent.putExtra("expiry", ingredient.getExpiryDate());
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
            @Override
            public void onDeleteClick(Ingredient ingredient) {
                deleteIngredientWithUndo(ingredient, view, fab);
            }
        });

        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Ingredient ingredientToDelete = adapter.getIngredientAt(position);
                    deleteIngredientWithUndo(ingredientToDelete, view, fab);
                }
            }
        }).attachToRecyclerView(recyclerView);

        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);
        viewModel.getAllIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            adapter.submitList(ingredients);
            textEmpty.setVisibility(ingredients.isEmpty() ? View.VISIBLE : View.GONE);
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return view;
    }

    private void deleteIngredientWithUndo(Ingredient ingredient, View view, FloatingActionButton fab) {
        viewModel.delete(ingredient);
        Snackbar.make(view, ingredient.getName() + " deleted", Snackbar.LENGTH_LONG)
                .setAnchorView(fab)
                .setAction("Undo", v -> viewModel.insert(ingredient))
                .show();
    }
}
