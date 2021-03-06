package com.kqp.awaken.client.entity;

import com.kqp.awaken.entity.mob.RaptorChickenEntity;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * Renderer for the raptor chicken.
 */
public class RaptorChickenRenderer<T extends MobEntity> extends MobEntityRenderer<T, ChickenEntityModel<T>> {
    public RaptorChickenRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new ChickenEntityModel<>(), 0.45F);
    }

    @Override
    public Identifier getTexture(T entity) {
        return Awaken.id("textures/entity/raptor_chicken.png");
    }

    /**
     * This method is actually overriden using a mixin.
     * See {@link net.minecraft.client.render.entity.LivingEntityRenderer}
     *
     * @param chickenEntity Raptor chicken entity
     * @param f             tickDelta
     * @return something???
     */
    public float getAnimationProgress(RaptorChickenEntity chickenEntity, float f) {
        float g = MathHelper.lerp(f, chickenEntity.field_6736, chickenEntity.field_6741);
        float h = MathHelper.lerp(f, chickenEntity.field_6738, chickenEntity.field_6743);
        return (MathHelper.sin(g) + 1.0F) * h;
    }

    @Override
    protected void scale(T entity, MatrixStack matrices, float tickDelta) {
        matrices.scale(1.5F, 1.65F, 1.5F);
    }
}
