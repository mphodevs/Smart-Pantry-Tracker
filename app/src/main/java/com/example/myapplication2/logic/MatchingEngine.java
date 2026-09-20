package com.example.myapplication2.logic;

import com.example.myapplication2.model.Ingredient;
import com.example.myapplication2.model.RecipeIngredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingEngine {

    private static final Map<String, String[]> SUBSTITUTES = new HashMap<>();

    static {
        // Map canonical items to their list of valid equivalents/substitutes
        SUBSTITUTES.put("butter", new String[]{"margarine", "butter", "oil"});
        SUBSTITUTES.put("margarine", new String[]{"butter", "margarine"});
        SUBSTITUTES.put("milk", new String[]{"milk", "cream", "almond milk", "soy milk"});
        SUBSTITUTES.put("sugar", new String[]{"sugar", "brown sugar", "honey", "sweetener"});
        SUBSTITUTES.put("olive oil", new String[]{"olive oil", "vegetable oil", "oil", "butter"});
        SUBSTITUTES.put("garlic", new String[]{"garlic", "garlic powder"});
        SUBSTITUTES.put("onion", new String[]{"onion", "onion powder", "shallot"});
    }

    /**
     * Checks if a recipe can be made with the current pantry.
     * Strict matching with substitute parameters: every required ingredient or an equivalent
     * must be present in sufficient quantity.
     */
    public static boolean canMakeRecipe(List<RecipeIngredient> required, List<Ingredient> pantry) {
        Map<String, Double> pantryMap = new HashMap<>();
        for (Ingredient item : pantry) {
            String normalizedName = normalize(item.getName());
            pantryMap.put(normalizedName, pantryMap.getOrDefault(normalizedName, 0.0) + item.getQuantity());
        }

        for (RecipeIngredient req : required) {
            String normalizedReqName = normalize(req.getIngredientName());
            
            // Check primary ingredient
            Double primaryQtyObj = pantryMap.get(normalizedReqName);
            double totalAvailable = (primaryQtyObj != null) ? primaryQtyObj : 0.0;

            // If primary ingredient is insufficient, check predefined substitutes
            if (totalAvailable < req.getRequiredQuantity()) {
                String[] subs = SUBSTITUTES.get(normalizedReqName);
                if (subs != null) {
                    for (String substitute : subs) {
                        String normalizedSub = normalize(substitute);
                        if (!normalizedSub.equals(normalizedReqName)) {
                            Double subQtyObj = pantryMap.get(normalizedSub);
                            totalAvailable += (subQtyObj != null) ? subQtyObj : 0.0;
                        }
                    }
                }
            }

            if (totalAvailable < req.getRequiredQuantity()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Normalizes ingredient names to handle simple pluralization and case differences.
     */
    public static String normalize(String name) {
        if (name == null) return "";
        String normalized = name.trim().toLowerCase();
        
        // Simple plural handling (very basic)
        if (normalized.endsWith("es")) {
            return normalized.substring(0, normalized.length() - 2);
        } else if (normalized.endsWith("s")) {
            return normalized.substring(0, normalized.length() - 1);
        }
        
        return normalized;
    }
}
