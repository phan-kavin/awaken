package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.client.TerminusClient;
import com.kqp.terminus.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.prism.impl.BufferUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.DoubleBuffer;

/**
 * Used to add the navigation tabs to reach the Terminus crafting screen.
 */
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeContainer> {
    private static final Identifier TEXTURE = new Identifier(Terminus.MOD_ID, "textures/gui/container/crafting.png");

    public InventoryScreenMixin(CreativeInventoryScreen.CreativeContainer container, PlayerInventory playerInventory, Text text) {
        super(container, playerInventory, text);
    }

    @Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
    public void catchMouseClickEvent(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (button == 0) {
            double aX = mouseX - this.x;
            double aY = mouseY - this.y;

            if (aX > 29 && aX < 57) {
                if (aY > -32 && aY < 0) {
                    TerminusClient.triggerOpenCraftingMenu();

                    callbackInfo.setReturnValue(true);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "drawBackground")
    public void drawCraftingTab(float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(this.x + 29, this.y - 28, 204, 0, 28, 32);

        this.setBlitOffset(100);
        this.itemRenderer.zOffset = 100.0F;
        RenderSystem.enableRescaleNormal();
        ItemStack itemStack = new ItemStack(Blocks.CRAFTING_TABLE);
        this.itemRenderer.renderGuiItem(itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.renderGuiItemOverlay(this.font, itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.zOffset = 0.0F;
        this.setBlitOffset(0);
    }

    @Inject(at = @At("TAIL"), method = "drawBackground")
    public void drawPlayerTab(float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(this.x, this.y - 28, 176, 32, 28, 32);
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void drawTooltips(int mouseX, int mouseY, float delta, CallbackInfo callbackInfo) {
        double aX = mouseX - this.x;
        double aY = mouseY - this.y;

        if (aY > -24 && aY < 0) {
            if (aX > 0 && aX < 28) {
                this.renderTooltip("Player", mouseX, mouseY);
            } else if (aX > 29 && aX < 57) {
                this.renderTooltip("Crafting", mouseX, mouseY);
            }
        }
    }
}
