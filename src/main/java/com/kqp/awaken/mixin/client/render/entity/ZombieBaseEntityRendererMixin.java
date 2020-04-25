package com.kqp.awaken.mixin.client.render.entity;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to override the zombie texture during blood moons.
 */
@Mixin(ZombieBaseEntityRenderer.class)
public class ZombieBaseEntityRendererMixin {
    private static final Identifier BLOOD_MOON_TEXTURE = new Identifier(Awaken.MOD_ID, "textures/entity/blood_moon/zombie.png");

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void overrideTextureForBloodMoon(ZombieEntity zombieEntity, CallbackInfoReturnable<Identifier> callbackInfo) {
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(MinecraftClient.getInstance().world);

        if (awakenLevelData.isBloodMoonActive()) {
            callbackInfo.setReturnValue(BLOOD_MOON_TEXTURE);
        }
    }
}