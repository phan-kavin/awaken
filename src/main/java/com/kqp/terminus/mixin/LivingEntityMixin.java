package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.data.TerminusWorldProperties;
import com.kqp.terminus.item.sword.AtlanteanSabreItem;
import com.kqp.terminus.util.Broadcaster;
import com.kqp.terminus.util.JsonUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

/**
 * Used to:
 * Detect deaths for triggering the awakening.
 * Deny riptide status when using the Atlantean sword.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        LivingEntity le = (LivingEntity) (Object) this;
        World world = le.world;

        if (!world.isClient) {
            Broadcaster broadcaster = new Broadcaster();

            if (source.getAttacker() instanceof PlayerEntity) {
                if (le instanceof CowEntity && !Terminus.worldProperties.isPostDragon()) {
                    Terminus.worldProperties.setPostDragon();
                    broadcaster.broadcastMessage("The ground shakes beneath you...", Formatting.DARK_RED, false, true);
                } else if (le instanceof SheepEntity && !Terminus.worldProperties.isPostWither()) {
                    Terminus.worldProperties.setPostWither();
                    broadcaster.broadcastMessage("Screams echo from below...", Formatting.DARK_RED, false, true);
                } else if (le instanceof PigEntity && !Terminus.worldProperties.isPostElderGuardian()) {
                    Terminus.worldProperties.setPostElderGuardian();
                    broadcaster.broadcastMessage("A sharp chill goes down your spine...", Formatting.DARK_RED, false, true);
                } else if (le instanceof ZombieEntity && !Terminus.worldProperties.isPostRaid()) {
                    Terminus.worldProperties.setPostRaid();
                    broadcaster.broadcastMessage("A distant figure fades into the shadows...", Formatting.DARK_RED, false, true);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "isUsingRiptide", cancellable = true)
    public void fixRiptideReturn(CallbackInfoReturnable<Boolean> callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.getMainHandStack().getItem() instanceof AtlanteanSabreItem) {
            callbackInfo.setReturnValue(false);
        }
    }
}
