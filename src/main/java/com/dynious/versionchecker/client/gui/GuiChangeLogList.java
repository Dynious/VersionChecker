package com.dynious.versionchecker.client.gui;

import com.dynious.versionchecker.lib.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiChangeLogList extends GuiScroll
{
    private GuiUpdates parent;
    private List<String> changeLogLines;
    protected int emptyLineHeight = 4;

    public GuiChangeLogList(GuiUpdates parent, int width, int height, int top, int bottom, int left)
    {
        super(Minecraft.getMinecraft(), width, height, top, bottom, left, parent.getFontRenderer().FONT_HEIGHT);
        this.parent = parent;
        this.emptyLineHeight = (int) (parent.getFontRenderer().FONT_HEIGHT / 2f);
        setPadding(1, 4);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_22243_3_)
    {
        super.drawScreen(mouseX, mouseY, p_22243_3_);
    }

    public void setText(String lines)
    {
        changeLogLines = new ArrayList<String>();
        final int MAX_LINE_LENGTH = listWidth - 10;

        for (String line : lines.split("\\n")) // Split string into lines
        {
            if (line.isEmpty())
                changeLogLines.add(line);
            else
            {
                String currentLine = "";

                for (String word : line.split(" ")) // Split each line into words by space
                {
                    if (this.parent.getFontRenderer().getStringWidth(currentLine + word) < MAX_LINE_LENGTH)
                    {
                        currentLine += word + " ";
                    }
                    else
                    {
                        if (this.parent.getFontRenderer().getStringWidth(word) < MAX_LINE_LENGTH)
                        {
                            changeLogLines.add(currentLine);
                            currentLine = word + " ";
                        }
                        else
                        {
                            while (this.parent.getFontRenderer().getStringWidth(word) >= MAX_LINE_LENGTH)
                            {
                                String cutWord = this.parent.getFontRenderer().trimStringToWidth(word, MAX_LINE_LENGTH);
                                changeLogLines.add(cutWord);
                                word = word.substring(cutWord.length());
                            }

                            currentLine = word + " ";
                        }
                    }
                }
                if (!currentLine.isEmpty())
                    changeLogLines.add(currentLine);
            }
            // add a gap between each line
            changeLogLines.add("");
        }
        // remove trailing gap
        if (!changeLogLines.isEmpty() && changeLogLines.get(changeLogLines.size() - 1).isEmpty())
            changeLogLines.remove(changeLogLines.size() - 1);
    }

    @Override
    protected int getSize()
    {
        return changeLogLines != null ? changeLogLines.size() : 0;
    }

    @Override
    protected int getSlotHeight(int slotIndex)
    {
        if (slotIndex >= 0 && slotIndex < changeLogLines.size() && changeLogLines.get(slotIndex).isEmpty())
            return emptyLineHeight;
        else
            return super.getSlotHeight(slotIndex);
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
    protected void drawSlot(int slotIndex, int minX, int maxX, int minY, int maxY, Tessellator tesselator)
    {
        if (slotIndex < changeLogLines.size())
        {
            this.parent.getFontRenderer().drawString(changeLogLines.get(slotIndex), minX + 3, minY, 0xFFFFFF);
        }
    }

    @Override
    public void overlayBackground()
    {
        this.client.renderEngine.bindTexture(Resources.GUI_WINDOW);
        GL11.glColor4f(0.6F, 0.6F, 0.6F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(left - 10, top - slotHeight, 0, 30, listWidth + 20, slotHeight, 220, 160);
        Gui.drawModalRectWithCustomSizedTexture(left - 10, top + listHeight, 0, listHeight + slotHeight, listWidth + 20, slotHeight, 220, 160);
    }
}
