package com.example.myapplication2.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication2.R;
import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.viewmodel.PantryViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private TextInputLayout layoutName, layoutQuantity, layoutUnit, layoutDate;
    private TextInputEditText editName, editQuantity, editUnit, editDate;
    private long selectedExpiryDate = 0;
    private PantryViewModel viewModel;
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        layoutName = findViewById(R.id.layout_text_name);
        layoutQuantity = findViewById(R.id.layout_text_quantity);
        layoutUnit = findViewById(R.id.layout_text_unit);
        layoutDate = findViewById(R.id.layout_text_date);

        editName = findViewById(R.id.edit_text_name);
        editQuantity = findViewById(R.id.edit_text_quantity);
        editUnit = findViewById(R.id.edit_text_unit);
        editDate = findViewById(R.id.edit_text_date);
        Button buttonSave = findViewById(R.id.button_save);

        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        if (getIntent().hasExtra("id")) {
            ingredientId = getIntent().getIntExtra("id", -1);
            editName.setText(getIntent().getStringExtra("name"));
            editQuantity.setText(String.valueOf(getIntent().getDoubleExtra("quantity", 0.0)));
            editUnit.setText(getIntent().getStringExtra("unit"));
            selectedExpiryDate = getIntent().getLongExtra("expiry", 0);
            if (selectedExpiryDate > 0) {
                updateDateDisplay();
            }
        }

        setupTextWatchers();

        editDate.setOnClickListener(v -> showDatePicker());
        layoutDate.setEndIconOnClickListener(v -> showDatePicker());
        buttonSave.setOnClickListener(v -> saveIngredient());
    }

    private void setupTextWatchers() {
        editName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    layoutName.setError("Ingredient name is required");
                } else {
                    layoutName.setError(null);
                }
            }
        });

        editQuantity.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().trim();
                if (str.isEmpty()) {
                    layoutQuantity.setError("Quantity is required");
                } else {
                    try {
                        double val = Double.parseDouble(str);
                        if (val <= 0) {
                            layoutQuantity.setError("Quantity must be greater than zero");
                        } else {
                            layoutQuantity.setError(null);
                        }
                    } catch (NumberFormatException e) {
                        layoutQuantity.setError("Please enter a valid number");
                    }
                }
            }
        });

        editUnit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    layoutUnit.setError("Unit is required (e.g., pcs, g, kg, cup)");
                } else {
                    layoutUnit.setError(null);
                }
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (selectedExpiryDate > 0) {
            calendar.setTimeInMillis(selectedExpiryDate);
        }
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedExpiryDate = calendar.getTimeInMillis();
            updateDateDisplay();
            validateDate();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        editDate.setText(sdf.format(new Date(selectedExpiryDate)));
    }

    private boolean validateDate() {
        if (selectedExpiryDate > 0) {
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar selected = Calendar.getInstance();
            selected.setTimeInMillis(selectedExpiryDate);
            selected.set(Calendar.HOUR_OF_DAY, 0);
            selected.set(Calendar.MINUTE, 0);
            selected.set(Calendar.SECOND, 0);
            selected.set(Calendar.MILLISECOND, 0);

            if (selected.before(today)) {
                layoutDate.setError("Expiry date cannot be in the past");
                return false;
            }
        }
        layoutDate.setError(null);
        return true;
    }

    private boolean validateInputs() {
        boolean isValid = true;

        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String quantityStr = editQuantity.getText() != null ? editQuantity.getText().toString().trim() : "";
        String unit = editUnit.getText() != null ? editUnit.getText().toString().trim() : "";

        // Validate Name
        if (name.isEmpty()) {
            layoutName.setError("Ingredient name is required");
            isValid = false;
        } else {
            layoutName.setError(null);
        }

        // Validate Quantity
        if (quantityStr.isEmpty()) {
            layoutQuantity.setError("Quantity is required");
            isValid = false;
        } else {
            try {
                double quantity = Double.parseDouble(quantityStr);
                if (quantity <= 0) {
                    layoutQuantity.setError("Quantity must be greater than zero");
                    isValid = false;
                } else {
                    layoutQuantity.setError(null);
                }
            } catch (NumberFormatException e) {
                layoutQuantity.setError("Please enter a valid number");
                isValid = false;
            }
        }

        // Validate Unit
        if (unit.isEmpty()) {
            layoutUnit.setError("Unit is required (e.g., pcs, g, kg, cup)");
            isValid = false;
        } else {
            layoutUnit.setError(null);
        }

        // Validate Date
        if (!validateDate()) {
            isValid = false;
        }

        return isValid;
    }

    private void saveIngredient() {
        if (!validateInputs()) {
            return;
        }

        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String quantityStr = editQuantity.getText() != null ? editQuantity.getText().toString().trim() : "0";
        double quantity = Double.parseDouble(quantityStr);
        String unit = editUnit.getText() != null ? editUnit.getText().toString().trim() : "";

        Ingredient ingredient = new Ingredient(name, quantity, unit, selectedExpiryDate);
        if (ingredientId != -1) {
            ingredient.setId(ingredientId);
            viewModel.update(ingredient);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.insert(ingredient);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        }
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
