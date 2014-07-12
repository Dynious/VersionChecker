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

    public GuiChangeLogList(GuiUpdates parent, int width, int height, int top, int bottom, int left)
    {
        super(Minecraft.getMinecraft(), width, height, top, bottom, left, 12);
        this.parent = parent;
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

        String currentLine = "";
        for (String line : lines.split("\\n")) // Split string into lines
        {
            for (String word : line.split("\\s+")) // Split each line into words by space
            {
                if (this.parent.getFontRenderer().getStringWidth(currentLine + word) < MAX_LINE_LENGTH)
                {
                    currentLine += word + " ";
                }
                else
                {
                    changeLogLines.add(currentLine);
                    if (this.parent.getFontRenderer().getStringWidth(word) < MAX_LINE_LENGTH)
                    {
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

                        if (word.length() > 0) // If we still have left over characters in the word
                            changeLogLines.add(word);
                    }
                }
            }
            changeLogLines.add(currentLine);
            currentLine = "";
        }
    }

    @Override
    protected int getSize()
    {
        return changeLogLines != null ? changeLogLines.size() : 0;
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
        if (index < changeLogLines.size())
        {
            this.parent.getFontRenderer().drawString(changeLogLines.get(index), this.left + 3, y + 2, 0xFFFFFF);
        }
    }

    @Override
    public void overlayBackground()
    {
        this.client.renderEngine.bindTexture(Resources.GUI_WINDOW);
        GL11.glColor4f(0.6F, 0.6F, 0.6F, 1.0F);
        Gui.func_146110_a(left - 10, top - slotHeight, 0, 30, listWidth + 20, slotHeight, 220, 160);
        Gui.func_146110_a(left - 10, top + listHeight, 0, listHeight + slotHeight, listWidth + 20, slotHeight, 220, 160);
    }
}
