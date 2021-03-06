package com.kqp.awaken.item.trinket;

import com.kqp.awaken.client.trinket.TrinketRenderer;
import com.kqp.awaken.client.trinket.TrinketRenderers;
import com.kqp.awaken.effect.ActiveEntityFeatureGroupProvider;
import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.util.TooltipUtil;
import dev.emi.trinkets.api.Trinket;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AwakenTrinketItem extends Item implements Trinket, ActiveEntityFeatureGroupProvider {
    public final String trinketGroup, trinketSlot;

    private final EntityFeatureGroup entityFeatureGroup;

    private final String rendererId;

    public AwakenTrinketItem(String trinketGroup, String trinketSlot, EntityFeatureGroup entityFeatureGroup, String rendererId) {
        super(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));

        this.trinketGroup = trinketGroup;
        this.trinketSlot = trinketSlot;

        this.entityFeatureGroup = entityFeatureGroup;

        this.rendererId = rendererId;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        TooltipUtil.addIterableTooltips(tooltip, this.getTranslationKey(), Formatting.YELLOW);

        List<Text> efgTooltips = new ArrayList();
        entityFeatureGroup.populateTooltips(efgTooltips);

        if (efgTooltips.size() > 0) {
            tooltip.add(new LiteralText(""));
            tooltip.add(new TranslatableText("item.equipped").formatted(Formatting.GRAY));
            tooltip.addAll(efgTooltips);
        }
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return group.equals(trinketGroup) && slot.equals(trinketSlot);
    }

    @Override
    public void onEquip(PlayerEntity player, ItemStack stack) {
        entityFeatureGroup.applyTo(player);
    }

    @Override
    public void onUnequip(PlayerEntity player, ItemStack stack) {
        entityFeatureGroup.removeFrom(player);
    }

    @Override
    public List<EntityFeatureGroup> getActiveEntityFeatureGroups(PlayerEntity player) {
        return Arrays.asList(entityFeatureGroup);
    }

    @Override
    public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
        if (rendererId != null && !rendererId.isEmpty()) {
            TrinketRenderer renderer = TrinketRenderers.getTrinketRenderer(rendererId);

            renderer.render(slot, matrixStack, vertexConsumer, light, model, player, headYaw, headPitch);
        }
    }
}
