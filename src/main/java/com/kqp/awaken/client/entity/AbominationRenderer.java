package com.kqp.awaken.client.entity;

import com.kqp.awaken.client.model.AbominationEntityModel;
import com.kqp.awaken.entity.mob.AbominationEntity;
import com.kqp.awaken.init.Awaken;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AbominationRenderer<T extends AbominationEntity, M extends BipedEntityModel<T>> extends BipedEntityRenderer<T, M> {
    private static final Identifier TEXTURE = Awaken.id("textures/entity/abomination.png");

    public AbominationRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, (M) new AbominationEntityModel(), 1.5F);
    }

    public Identifier getTexture(AbominationEntity abominationEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(T entity, MatrixStack matrices, float tickDelta) {
        matrices.scale(3F, 3F, 3F);
    }

    @Override
    protected boolean isShaking(AbominationEntity abominationEntity) {
        return abominationEntity.isDespawning();
    }
}
