package com.kqp.awaken.mixin;

import com.kqp.awaken.item.effect.SpecialItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

/**
 * Used to:
 * Apply damage buffs
 * Detect item equips/unequips
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    private static final HashMap<PlayerEntity, DefaultedList<ItemStack>> PLAYER_ARMOR_TRACKER = new HashMap();
    private static final HashMap<PlayerEntity, ItemStack> PLAYER_HELD_TRACKER = new HashMap();

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void detectEquippableArmor(CallbackInfo callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.world.isClient()) {
            DefaultedList<ItemStack> curr = player.inventory.armor;

            if (PLAYER_ARMOR_TRACKER.containsKey(player)) {
                DefaultedList<ItemStack> prev = PLAYER_ARMOR_TRACKER.get(player);

                for (int i = 0; i < curr.size(); i++) {
                    ItemStack currItemStack = curr.get(i);
                    ItemStack prevItemStack = prev.get(i);

                    if (!ItemStack.areItemsEqual(currItemStack, prevItemStack)) {
                        if (SpecialItemRegistry.EQUIPPABLE_ARMOR.containsKey(prevItemStack.getItem())) {
                            SpecialItemRegistry.EQUIPPABLE_ARMOR.get(prevItemStack.getItem()).unEquip(prevItemStack, player);
                        }

                        if (SpecialItemRegistry.EQUIPPABLE_ARMOR.containsKey(currItemStack.getItem())) {
                            SpecialItemRegistry.EQUIPPABLE_ARMOR.get(currItemStack.getItem()).equip(currItemStack, player);
                        }
                    }
                }
            }

            DefaultedList<ItemStack> clone = DefaultedList.ofSize(4, ItemStack.EMPTY);
            for (int i = 0; i < clone.size(); i++) {
                clone.set(i, curr.get(i).copy());
            }

            PLAYER_ARMOR_TRACKER.put(player, clone);
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void detectEquippableItems(CallbackInfo callbackInfo) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (!player.world.isClient()) {
            ItemStack curr = player.inventory.getMainHandStack();

            if (PLAYER_HELD_TRACKER.containsKey(player)) {
                ItemStack prev = PLAYER_HELD_TRACKER.get(player);

                if (!ItemStack.areItemsEqual(curr, prev)) {
                    if (SpecialItemRegistry.EQUIPPABLE_ITEM.containsKey(prev.getItem())) {
                        SpecialItemRegistry.EQUIPPABLE_ITEM.get(prev.getItem()).unEquip(prev, player);
                    }

                    if (SpecialItemRegistry.EQUIPPABLE_ITEM.containsKey(curr.getItem())) {
                        SpecialItemRegistry.EQUIPPABLE_ITEM.get(curr.getItem()).equip(curr, player);
                    }
                }
            }

            PLAYER_HELD_TRACKER.put(player, curr.copy());
        }
    }
}