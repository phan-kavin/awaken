package com.kqp.terminus.client.screen;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.client.container.TerminusCraftingContainer;
import com.kqp.terminus.client.container.TerminusResultSlot;
import com.kqp.terminus.recipe.Reagent;
import com.kqp.terminus.recipe.TerminusRecipe;
import com.kqp.terminus.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.List;

/**
 * Screen for Terminus's crafting system.
 */
public class TerminusCraftingScreen extends ContainerScreen<TerminusCraftingContainer> {
    public static final String TRANSLATION_KEY = Util.createTranslationKey("container", new Identifier(Terminus.MOD_ID, "terminus_crafting"));
    private static final Identifier TEXTURE = new Identifier(Terminus.MOD_ID, "textures/gui/container/crafting.png");

    /**
     * Position of the scroll bar.
     */
    public float scrollPosition = 0.0F;

    public TerminusCraftingScreen(TerminusCraftingContainer container, PlayerInventory playerInventory) {
        super(container, playerInventory, new TranslatableText(TRANSLATION_KEY));
        this.containerWidth = 176;
        this.containerHeight = 166;
        this.passEvents = false;

        syncScrollbar();
    }

    /**
     * Overriden to render the tooltips for the tabs.
     *
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        super.render(mouseX, mouseY, delta);
        this.drawMouseoverTooltip(mouseX, mouseY);

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

    /**
     * Overriden to add the reagents needed to craft the hovered slot.
     *
     * @param text
     * @param x
     * @param y
     */
    @Override
    public void renderTooltip(List<String> text, int x, int y) {
        if (this.focusedSlot instanceof TerminusResultSlot && container.recipes != null) {
            int currentIndex = ((TerminusResultSlot) this.focusedSlot).currentIndex;

            if (container.recipes.size() > currentIndex) {
                TerminusRecipe recipe = container.recipes.get(currentIndex);

                text.add("---");
                text.add("To Craft: ");
                for (Reagent reagent : recipe.reagents.keySet()) {
                    String reagentLine = recipe.reagents.get(reagent) + " x " + reagent.toString();
                    List<String> split = this.font.wrapStringToWidthAsList(reagentLine, this.containerWidth - 50);

                    text.add(split.get(0));

                    // Used to bump up the wrapped lines
                    int offset = (recipe.reagents.get(reagent) + " x ").length();

                    if (split.size() > 1) {
                        for (int i = 1; i < split.size(); i++) {
                            // Cool trick to insert 'offset' amount of spaces
                            text.add(String.format("%" + offset + "s", "") + split.get(i));
                        }
                    }
                }
            }
        }


        super.renderTooltip(text, x, y);
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.font.draw(this.title.asFormattedString(), 8.0F, 8.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float) (this.containerHeight - 96 + 4), 4210752);
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.containerWidth) / 2;
        int j = (this.height - this.containerHeight) / 2;

        this.minecraft.getTextureManager().bindTexture(TEXTURE);

        this.blit(i, j - 28, 176, 0, 28, 32);

        this.blit(i, j, 0, 0, 176, 166);

        this.blit(i + 156, j + 18 + (int) ((float) (55 - 18) * this.scrollPosition), 232 + (this.hasScrollbar() ? 0 : 12), 0, 12, 15);

        this.blit(i + 29, j - 28, 204, 32, 28, 32);

        this.setBlitOffset(100);
        this.itemRenderer.zOffset = 100.0F;
        RenderSystem.enableRescaleNormal();
        ItemStack itemStack = new ItemStack(Blocks.CRAFTING_TABLE);
        this.itemRenderer.renderGuiItem(itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.renderGuiItemOverlay(this.font, itemStack, this.x + 29 + (28 - 15) / 2, this.y - 28 + 10);
        this.itemRenderer.zOffset = 0.0F;
        this.setBlitOffset(0);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double amount) {
        if (!this.hasScrollbar()) {
            return false;
        } else {
            int i = (this.container.outputs.size() + 8 - 1) / 8 - 3;
            this.scrollPosition = (float) ((double) this.scrollPosition - amount / (double) i);
            this.scrollPosition = MathHelper.clamp(this.scrollPosition, 0.0F, 1.0F);
            syncScrollbar();
            return true;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double aX = mouseX - this.x;
            double aY = mouseY - this.y;

            if (aX > 0 && aX < 28) {
                if (aY > -32 && aY < 0) {
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeDouble(MouseUtil.getMouseX());
                    buf.writeDouble(MouseUtil.getMouseY());

                    ClientSidePacketRegistry.INSTANCE.sendToServer(Terminus.TNetworking.CLOSE_CRAFTING_C2S_ID, buf);
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean hasScrollbar() {
        return this.container.shouldShowScrollbar();
    }

    /**
     * Sends the server the position of the scroll bar.
     */
    public void syncScrollbar() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeFloat(scrollPosition);

        ClientSidePacketRegistry.INSTANCE.sendToServer(Terminus.TNetworking.SYNC_SCROLLBAR_ID, buf);
    }
}
