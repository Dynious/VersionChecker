package com.dynious.versionchecker.client.gui;

import com.dynious.versionchecker.handler.UpdateHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonUpdates extends GuiButton
{
    public GuiButtonUpdates(int id, int x, int y)
    {
        super(id, x, y, 20, 20, String.valueOf(UpdateHandler.getListSize()));
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y)
    {
        this.displayString = String.valueOf(UpdateHandler.getListSize());
        super.drawButton(mc, x, y);
    }
}
