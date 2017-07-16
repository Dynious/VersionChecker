package com.dynious.versionchecker.client.gui;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.dynious.versionchecker.lib.Resources;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer; // Used to be WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public abstract class GuiScroll
{
    protected final Minecraft client;
    protected final int listWidth;
    protected final int listHeight;
    protected final int top;
    protected final int bottom;
    private final int right;
    protected final int left;
    protected final int slotHeight;
    private int scrollUpActionId;
    private int scrollDownActionId;
    protected int mouseX;
    protected int mouseY;
    private float initialMouseClickY = -2.0F;
    private float scrollFactor;
    private float scrollDistance;
    private int selectedIndex = -1;
    private long lastClickTime = 0L;
    private boolean isSelectable = true;
    private boolean hasCustomHeader;
    private int customHeaderHeight;
    public boolean disableInput = false;
    protected int horizontalPadding;
    protected int verticalPadding;

    public GuiScroll(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight)
    {
        this.client = client;
        this.listWidth = width;
        this.listHeight = height;
        this.top = top;
        this.bottom = bottom;
        this.slotHeight = entryHeight;
        this.left = left;
        this.right = width + this.left;
    }

    public void setSelectable(boolean isSelectable)
    {
        this.isSelectable = isSelectable;
    }

    protected void setHasCustomHeader(boolean hasCustomHeader, int customHeaderHeight)
    {
        this.hasCustomHeader = hasCustomHeader;
        this.customHeaderHeight = hasCustomHeader ? customHeaderHeight : 0;
    }

    public void setPadding(int horizontalPadding, int verticalPadding)
    {
        this.horizontalPadding = horizontalPadding;
        this.verticalPadding = verticalPadding;
    }

    protected abstract int getSize();

    protected int getSlotHeight(int slotIndex)
    {
        return slotHeight;
    }

    protected abstract void elementClicked(int index, boolean doubleClick);

    protected abstract boolean isSelected(int index);

    protected int getContentHeight()
    {
        int contentHeight = 0;
        for (int slotIndex = 0; slotIndex < getSize(); slotIndex++)
        {
            contentHeight += getSlotHeight(slotIndex);
        }
        return contentHeight + customHeaderHeight;
    }

    protected abstract void drawBackground();

    protected abstract void drawSlot(int slotIndex, int minX, int maxX, int minY, int maxY, Tessellator tesselator);

    protected void drawCustomHeader(int maxX, int topY, Tessellator tesselator)
    {
    }

    protected void onMouseDownOutsideOfSlots(int relativeX, int relativeY)
    {
    }

    protected void drawForeground(int mouseX, int mouseY)
    {
    }

    public int getSlotIndexAtPosition(int x, int y)
    {
        int minX = this.left + 1 + horizontalPadding;
        int maxX = this.left + this.listWidth - 7 - horizontalPadding;

        if (mouseX >= minX && mouseX <= maxX)
        {
            int relativeY = y - this.top - this.customHeaderHeight - verticalPadding + (int) this.scrollDistance;
            int currentTopY = 0;
            for (int slotIndex = 0; slotIndex < getSize(); slotIndex++)
            {
                int currentBottomY = currentTopY + getSlotHeight(slotIndex);

                if (relativeY >= currentTopY && relativeY < currentBottomY)
                    return slotIndex;

                currentTopY = currentBottomY;
            }
        }
        return -1;
    }

    public void registerScrollButtons(@SuppressWarnings("rawtypes") List p_22240_1_, int scrollUpActionId, int scrollDownActionId)
    {
        this.scrollUpActionId = scrollUpActionId;
        this.scrollDownActionId = scrollDownActionId;
    }

    private void applyScrollLimits()
    {
        float maxScrollDistance = getMaxScrollDistance();
        if (maxScrollDistance > 0)
            scrollDistance = Math.max(0, Math.min(getMaxScrollDistance(), scrollDistance));
        else
            scrollDistance = maxScrollDistance / 2;
    }

    public void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == this.scrollUpActionId)
            {
                this.scrollDistance -= (float) (this.slotHeight * 2 / 3);
                this.initialMouseClickY = -2.0F;
                this.applyScrollLimits();
            }
            else if (button.id == this.scrollDownActionId)
            {
                this.scrollDistance += (float) (this.slotHeight * 2 / 3);
                this.initialMouseClickY = -2.0F;
                this.applyScrollLimits();
            }
        }
    }

    public float getMaxScrollDistance()
    {
        return getContentHeight() - getVisibleHeight() + verticalPadding * 2;
    }

    public int getVisibleHeight()
    {
        return bottom - top;
    }

    public int getScrollBarHeight()
    {
        int scrollBarHeight = (int) ((float) (getVisibleHeight() * getVisibleHeight()) / (float) getContentHeight());
        scrollBarHeight = Math.max(32, Math.min(getVisibleHeight() - 8, scrollBarHeight));
        return scrollBarHeight;
    }

    public void drawScreen(int mouseX, int mouseY, float p_22243_3_)
    {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawBackground();
        int listLength = this.getSize();
        int scrollBarXStart = this.left + this.listWidth - 6;
        int scrollBarXEnd = scrollBarXStart + 6;
        int boxLeft = this.left + horizontalPadding;
        int boxRight = scrollBarXStart - 1 - horizontalPadding;

        if (!disableInput)
        {
            if (Mouse.isButtonDown(0))
            {
                // this is the case when the mouse was not down last frame
                if (this.initialMouseClickY == -1.0F)
                {
                    if (mouseX >= scrollBarXStart && mouseX <= scrollBarXEnd && mouseY >= this.top && mouseY <= this.bottom)
                    {
                        this.scrollFactor = -1.0F / ((float) (getVisibleHeight() - getScrollBarHeight()) / (float) Math.max(1, getMaxScrollDistance()));
                        this.initialMouseClickY = (float) mouseY;
                    }
                    else
                    {
                        int clickedSlotIndex = getSlotIndexAtPosition(mouseX, mouseY);
                        if (clickedSlotIndex >= 0)
                        {
                            boolean isDoubleClick = clickedSlotIndex == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250L;
                            this.elementClicked(clickedSlotIndex, isDoubleClick);
                            this.selectedIndex = clickedSlotIndex;
                            this.lastClickTime = System.currentTimeMillis();
                        }
                        else
                        {
                            this.onMouseDownOutsideOfSlots(mouseX - boxLeft, mouseY - this.top + (int) this.scrollDistance);
                        }
                        // -2 will disable scrolling until the mouse is released
                        this.initialMouseClickY = -2.0F;
                    }
                }
                else if (this.initialMouseClickY >= 0.0F)
                {
                    this.scrollDistance -= ((float) mouseY - this.initialMouseClickY) * this.scrollFactor;
                    this.initialMouseClickY = (float) mouseY;
                }
            }
            // mouse is not down
            else
            {
                // scroll wheel
                while (Mouse.next())
                {
                    int deltaZ = -Mouse.getEventDWheel();
                    deltaZ = deltaZ > 0 ? 1 : (deltaZ < 0 ? -1 : 0);
                    this.scrollDistance += (float) (getSize() < 20 ? deltaZ * this.slotHeight * 2 : deltaZ * this.getContentHeight() / 20f);
                }

                // will try to initialize scrolling on next mouse click
                this.initialMouseClickY = -1.0F;
            }
        }

        this.applyScrollLimits();
        Tessellator tess = Tessellator.getInstance();
        VertexBuffer  worldr = tess.getBuffer(); // Used to be  WorldRenderer worldr = tess.getWorldRenderer();
        if (this.client.world != null)
        {
            this.drawGradientRect(this.left, this.top, this.right, this.bottom, -1072689136, -804253680);
        }
        else
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_FOG);
            this.client.renderEngine.bindTexture(Gui.OPTIONS_BACKGROUND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float var17 = 32.0F;
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos((double) this.left, (double) this.bottom, 0.0D).tex( (double) ((float) this.left / var17), (double) ((float) (this.bottom + (int) this.scrollDistance) / var17) ).color(0x20, 0x20, 0x20,  255).endVertex();
            worldr.pos((double) this.right, (double) this.bottom, 0.0D).tex((double) ((float) this.right / var17), (double) ((float) (this.bottom + (int) this.scrollDistance) / var17)).color(0x20, 0x20, 0x20,  255).endVertex();
            worldr.pos((double) this.right, (double) this.top, 0.0D).tex((double) ((float) this.right / var17), (double) ((float) (this.top + (int) this.scrollDistance) / var17)).color(0x20, 0x20, 0x20,  255).endVertex();
            worldr.pos((double) this.left, (double) this.top, 0.0D).tex((double) ((float) this.left / var17), (double) ((float) (this.top + (int) this.scrollDistance) / var17)).color(0x20, 0x20, 0x20,  255).endVertex();
            tess.draw();
        }
        //        boxRight = this.listWidth / 2 - 92 - 16;
        int topOffsetByScrollDistance = this.top - (int) this.scrollDistance;

        if (this.hasCustomHeader)
        {
            this.drawCustomHeader(boxRight, topOffsetByScrollDistance, tess);
        }

        int drawY = topOffsetByScrollDistance + customHeaderHeight + verticalPadding;
        for (int slotIndex = 0; slotIndex < listLength; ++slotIndex)
        {
            int curSlotHeight = getSlotHeight(slotIndex);

            if (drawY <= this.bottom && drawY + curSlotHeight >= this.top)
            {
                if (this.isSelectable && this.isSelected(slotIndex))
                {
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

                    worldr.pos((double) boxLeft, (double) (drawY + curSlotHeight + 2), 0.0D).tex( 0.0D, 1.0D).color(0x80,0x80,0x80,255).endVertex();
                    worldr.pos((double) boxRight, (double) (drawY + curSlotHeight + 2), 0.0D).tex( 1.0D, 1.0D).color(0x80,0x80,0x80,255).endVertex();
                    worldr.pos((double) boxRight, (double) (drawY - 2), 0.0D).tex( 1.0D, 0.0D).color(0x80,0x80,0x80,255).endVertex();
                    worldr.pos((double) boxLeft, (double) (drawY - 2), 0.0D).tex( 0.0D, 0.0D).color(0x80,0x80,0x80,255).endVertex();

                    worldr.pos((double) (boxLeft + 1), (double) (drawY + curSlotHeight + 1), 0.0D).tex( 0.0D, 1.0D).color(0x80,0x80,0x80,255).endVertex();
                    worldr.pos((double) (boxRight - 1), (double) (drawY + curSlotHeight + 1), 0.0D).tex( 1.0D, 1.0D).color(0x80,0x80,0x80,255).endVertex();
                    worldr.pos((double) (boxRight - 1), (double) (drawY - 1), 0.0D).tex( 1.0D, 0.0D).color(0x80,0x80,0x80,255).endVertex();
                    worldr.pos((double) (boxLeft + 1), (double) (drawY - 1), 0.0D).tex(0.0D, 0.0D).color(0x80,0x80,0x80,255).endVertex();
                    
                    tess.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                }

                this.drawSlot(slotIndex, boxLeft, boxRight, drawY, drawY + curSlotHeight, tess);
            }

            drawY += curSlotHeight;
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        byte fadeGradientHeight = 4;
        if (this.client.world == null)
        {
            this.overlayBackground();
        }
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldr.pos((double) this.left, (double) (this.top + fadeGradientHeight), 0.0D).tex( 0.0D, 1.0D).color(0,0,0,255).endVertex();
        worldr.pos((double) this.right, (double) (this.top + fadeGradientHeight), 0.0D).tex( 1.0D, 1.0D).color(0,0,0,255).endVertex();
        worldr.pos((double) this.right, (double) this.top, 0.0D).tex( 1.0D, 0.0D).color(0,0,0,255).endVertex();
        worldr.pos((double) this.left, (double) this.top, 0.0D).tex( 0.0D, 0.0D).color(0,0,0,255).endVertex();
        tess.draw();
        worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        worldr.pos((double) this.left, (double) this.bottom, 0.0D).tex( 0.0D, 1.0D).color(0,0,0,255).endVertex();
        worldr.pos((double) this.right, (double) this.bottom, 0.0D).tex( 1.0D, 1.0D).color(0,0,0,255).endVertex();
        worldr.pos((double) this.right, (double) (this.bottom - fadeGradientHeight), 0.0D).tex( 1.0D, 0.0D).color(0,0,0,0).endVertex();
        worldr.pos((double) this.left, (double) (this.bottom - fadeGradientHeight), 0.0D).tex( 0.0D, 0.0D).color(0,0,0,0).endVertex();
        tess.draw();

        float maxScrollDistance = getMaxScrollDistance();
        if (maxScrollDistance > 0)
        {
            int scrollBarHeight = getScrollBarHeight();
            int scrollBarTop = (int) (this.scrollDistance * (getVisibleHeight() - scrollBarHeight) / maxScrollDistance + this.top);
            scrollBarTop = Math.max(top, scrollBarTop);

            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos((double) scrollBarXStart, (double) this.bottom, 0.0D).tex( 0.0D, 1.0D).color(0,0,0,255).endVertex();
            worldr.pos((double) scrollBarXEnd, (double) this.bottom, 0.0D).tex( 1.0D, 1.0D).color(0,0,0,255).endVertex();
            worldr.pos((double) scrollBarXEnd, (double) this.top, 0.0D).tex( 1.0D, 0.0D).color(0,0,0,255).endVertex();
            worldr.pos((double) scrollBarXStart, (double) this.top, 0.0D).tex( 0.0D, 0.0D).color(0,0,0,255).endVertex();
            tess.draw();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos((double) scrollBarXStart, (double) (scrollBarTop + scrollBarHeight), 0.0D).tex( 0.0D, 1.0D).color(0x80,0x80,0x80,255).endVertex();
            worldr.pos((double) scrollBarXEnd, (double) (scrollBarTop + scrollBarHeight), 0.0D).tex( 1.0D, 1.0D).color(0x80,0x80,0x80,255).endVertex();
            worldr.pos((double) scrollBarXEnd, (double) scrollBarTop, 0.0D).tex( 1.0D, 0.0D).color(0x80,0x80,0x80,255).endVertex();
            worldr.pos((double) scrollBarXStart, (double) scrollBarTop, 0.0D).tex( 0.0D, 0.0D).color(0x80,0x80,0x80,255).endVertex();
            tess.draw();
            worldr.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos((double) scrollBarXStart, (double) (scrollBarTop + scrollBarHeight - 1), 0.0D).tex( 0.0D, 1.0D).color(0xC0,0xC0,0xC0,255).endVertex();
            worldr.pos((double) (scrollBarXEnd - 1), (double) (scrollBarTop + scrollBarHeight - 1), 0.0D).tex( 1.0D, 1.0D).color(0xC0,0xC0,0xC0,255).endVertex();
            worldr.pos((double) (scrollBarXEnd - 1), (double) scrollBarTop, 0.0D).tex( 1.0D, 0.0D).color(0xC0,0xC0,0xC0,255).endVertex();
            worldr.pos((double) scrollBarXStart, (double) scrollBarTop, 0.0D).tex( 0.0D, 0.0D).color(0xC0,0xC0,0xC0,255).endVertex();
            tess.draw();
        }

        this.drawForeground(mouseX, mouseY);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void overlayBackground()
    {
        this.client.renderEngine.bindTexture(Resources.GUI_WINDOW);
        GL11.glColor4f(0.6F, 0.6F, 0.6F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(left - 10, top - slotHeight, 0, 0, listWidth + 20, slotHeight, 220, 160);
        Gui.drawModalRectWithCustomSizedTexture(left - 10, top + listHeight, 0, listHeight + slotHeight, listWidth + 20, slotHeight, 220, 160);
    }

    protected void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        int f = (par5 >> 24 & 255);
        float f1 = (float)(par5 >> 16 & 255) / 255.0F;
        float f2 = (float)(par5 >> 8 & 255) / 255.0F;
        float f3 = (float)(par5 & 255) / 255.0F;
        int f4 = (par6 >> 24 & 255);
        float f5 = (float)(par6 >> 16 & 255) / 255.0F;
        float f6 = (float)(par6 >> 8 & 255) / 255.0F;
        float f7 = (float)(par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer  worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos((double)par3, (double)par2, 0.0D).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)par1, (double)par2, 0.0D).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)par1, (double)par4, 0.0D).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double)par3, (double)par4, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
