package com.kqp.awaken.client.slot;

import com.kqp.awaken.inventory.AwakenCraftingResultInventory;
import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.ComparableItemStack;
import com.kqp.awaken.screen.AwakenCraftingScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.HashMap;
import java.util.List;

/**
 * Slot for crafting results.
 */
public class AwakenCraftingResultSlot extends Slot {
    private final AwakenCraftingScreenHandler screenHandler;

    public PlayerInventory playerInventory;

    public int currentIndex = 0;

    public AwakenCraftingResultSlot(AwakenCraftingScreenHandler screenHandler,
                                    AwakenCraftingResultInventory craftingResultInventory,
                                    PlayerInventory playerInventory,
                                    int invSlot, int xPosition, int yPosition) {
        super(craftingResultInventory, invSlot, xPosition, yPosition);
        this.screenHandler = screenHandler;
        this.playerInventory = playerInventory;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack takeStack(int amount) {
        return super.takeStack(amount);
    }

    @Override
    protected void onCrafted(ItemStack itemStack, int amount) {
        onCrafted(itemStack);
    }

    @Override
    protected void onTake(int amount) {
    }

    /**
     * Called whenever an item is taken from this slot.
     *
     * @param stack ItemStack that is being crafted
     */
    @Override
    protected void onCrafted(ItemStack stack) {
        if (!stack.isEmpty()) {
            AwakenRecipe recipe = screenHandler.craftingResultRecipes.get(currentIndex);
            recipe.onCraft(playerInventory);
        }

        markDirty();
        screenHandler.updateCraftingResults();
    }

    @Override
    public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);

        return stack;
    }
}
