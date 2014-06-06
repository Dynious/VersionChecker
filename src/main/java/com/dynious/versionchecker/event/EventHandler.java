package com.dynious.versionchecker.event;

import com.dynious.versionchecker.client.gui.GuiMainMenuHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;

public class EventHandler
{
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent evt)
    {
        if (evt.gui instanceof GuiMainMenu)
        {
            GuiMainMenuHandler.initGui(evt.gui, evt.buttonList);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void drawScreen(GuiScreenEvent.DrawScreenEvent.Post evt)
    {
        if (evt.gui instanceof GuiMainMenu)
        {
            GuiMainMenuHandler.drawScreen(evt.gui);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent evt)
    {
        if (evt.gui instanceof GuiMainMenu)
        {
            GuiMainMenuHandler.onActionPerformed(evt.button);
        }
    }
}
