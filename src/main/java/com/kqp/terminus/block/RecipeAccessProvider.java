package com.kqp.terminus.block;

import com.kqp.terminus.recipe.RecipeType;

/**
 * Used only by blocks that will enable players access to specified recipe types.
 * See {@link RecipeType} for all valid types.
 */
public interface RecipeAccessProvider {
    /**
     * Types of recipes that the implementing block will provide.
     *
     * @return String[] of recipe types
     */
    String[] getRecipeTypes();
}