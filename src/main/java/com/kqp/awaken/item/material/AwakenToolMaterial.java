package com.kqp.awaken.item.material;

import com.kqp.awaken.init.AwakenItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

/**
 * Used to create custom tool materials.
 */
public enum AwakenToolMaterial implements ToolMaterial {
    CELESTIAL_STEEL(4, 1000, 10.0F, 10.0F, 18, () -> {
        return Ingredient.ofItems(AwakenItems.CELESTIAL_STEEL_INGOT);
    }),
    PHASE_1_SPECIAL_SWORD(2, 750, 8.0F, 3F, 10, () -> {
        return Ingredient.ofItems();
    }),
    PHASE_1_SPECIAL_TOOL(2, 750, 7.0F, 4F, 10, () -> {
        return Ingredient.ofItems();
    }),
    JANG_KATANA(-1, -1, -1.0F, 14F, -1, () -> {
        return Ingredient.ofItems();
    });

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    AwakenToolMaterial(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return miningLevel;
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
}