package com.example.myapplication2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication2.R;
import com.example.myapplication2.model.Ingredient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IngredientAdapter extends ListAdapter<Ingredient, IngredientAdapter.IngredientViewHolder> {

    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
        void onDeleteClick(Ingredient ingredient);
    }

    public IngredientAdapter(@NonNull DiffUtil.ItemCallback<Ingredient> diffCallback, OnIngredientClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient current = getItem(position);
        holder.bind(current, listener);
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final TextView textName;
        private final TextView textQuantity;
        private final TextView textExpiry;
        private final ImageButton buttonDelete;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textQuantity = itemView.findViewById(R.id.text_quantity);
            textExpiry = itemView.findViewById(R.id.text_expiry);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }

        public void bind(Ingredient ingredient, OnIngredientClickListener listener) {
            textName.setText(ingredient.getName());
            textQuantity.setText(ingredient.getQuantity() + " " + ingredient.getUnit());
            
            if (ingredient.getExpiryDate() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                textExpiry.setText("Expires: " + sdf.format(new Date(ingredient.getExpiryDate())));
                textExpiry.setVisibility(View.VISIBLE);

                // Calculate date threshold warnings (e.g. within 3 days)
                long currentTime = System.currentTimeMillis();
                long diffInMillis = ingredient.getExpiryDate() - currentTime;
                long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

                if (diffInMillis < 0) {
                    textExpiry.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.expiry_expired));
                    textExpiry.setText("Expired! (" + sdf.format(new Date(ingredient.getExpiryDate())) + ")");
                } else if (diffInDays <= 3) {
                    textExpiry.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.expiry_warning));
                    textExpiry.setText("Expires soon: " + sdf.format(new Date(ingredient.getExpiryDate())));
                } else {
                    textExpiry.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.expiry_safe));
                }
            } else {
                textExpiry.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> listener.onIngredientClick(ingredient));
            buttonDelete.setOnClickListener(v -> listener.onDeleteClick(ingredient));
        }
    }

    public static class IngredientDiff extends DiffUtil.ItemCallback<Ingredient> {
        @Override
        public boolean areItemsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ingredient oldItem, @NonNull Ingredient newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getQuantity() == newItem.getQuantity() &&
                    oldItem.getUnit().equals(newItem.getUnit()) &&
                    oldItem.getExpiryDate() == newItem.getExpiryDate();
        }
    }
}
