package com.dynious.versionchecker.client.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMainMenuHandler
{
    public static final int BUTTON_ID = 404;

    public static void initGui(GuiScreen gui, List<GuiButton> buttonList)
    {
        for (GuiButton button : buttonList)
        {
            if (button instanceof GuiButtonUpdates)
            {
                return;
            }
        }
        GuiButtonUpdates button = new GuiButtonUpdates(BUTTON_ID, gui.width / 2 - 124, gui.height / 4 + 96);
        buttonList.add(button);
    }

    public static void onActionPerformed(GuiButton gui)
    {
        if (gui.id == BUTTON_ID)
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiUpdates());
        }
    }
}
