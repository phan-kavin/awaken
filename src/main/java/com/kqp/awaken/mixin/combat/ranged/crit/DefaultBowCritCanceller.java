package com.kqp.awaken.mixin.combat.ranged.crit;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to stop the bow item from setting the critical flag in the projectile entity.
 */
@Mixin(BowItem.class)
public class DefaultBowCritCanceller {
    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setCritical(Z)V"))
    private void cancelCritSet(PersistentProjectileEntity projectile, boolean critical) {
    }
}
