package com.dynious.versionchecker.client.gui;

import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class GuiChangeLog extends GuiScroll
{
    private GuiUpdates parent;
    private String[] textArray;
    private int ticker;

    public GuiChangeLog(GuiUpdates parent, int width, int height, int top, int bottom, int left)
    {
        super(Minecraft.getMinecraft(), width, height, top, bottom, left, 12);
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_22243_3_)
    {
        ticker++;
        super.drawScreen(mouseX, mouseY, p_22243_3_);
    }

    public void setText(String string)
    {
        textArray = string.split("\\n");
    }

    @Override
    protected int getSize()
    {
        return textArray != null ? textArray.length : 0;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
    }

    @Override
    protected boolean isSelected(int index)
    {
        return false;
    }

    @Override
    protected void drawBackground()
    {
        parent.drawWindow();
    }

    @Override
    protected void drawSlot(int index, int x, int y, int var4, Tessellator var5)
    {
        if (index < textArray.length)
        {
            int trimmedLength = textArray[index].length() - this.parent.getFontRenderer().trimStringToWidth(textArray[index], listWidth - 10).length();
            if (trimmedLength == 0)
            {
                this.parent.getFontRenderer().drawString(textArray[index], this.left + 3, y + 2, 0xFFFFFF);
            }
            else
            {
                String spaces = "                                               ";
                trimmedLength += 35 + spaces.length();
                String string = this.parent.getFontRenderer().trimStringToWidth((spaces + textArray[index]).substring(ticker / 5 % trimmedLength), listWidth - 10);
                this.parent.getFontRenderer().drawString(string, this.left + 3, y + 2, 0xFFFFFF);
            }
        }
    }


}
