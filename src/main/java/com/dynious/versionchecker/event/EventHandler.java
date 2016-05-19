package com.dynious.versionchecker.event;

import com.dynious.versionchecker.client.gui.GuiMainMenuHandler;
import com.dynious.versionchecker.handler.IMCHandler;
import com.dynious.versionchecker.handler.UpdateHandler;
import com.dynious.versionchecker.lib.Reference;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;

public class EventHandler
{
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent evt)
    {
        if (evt.getGui() instanceof GuiMainMenu)
        {
            IMCHandler.processMessages(FMLInterModComms.fetchRuntimeMessages(Reference.MOD_ID));
            GuiMainMenuHandler.initGui(evt.getGui(), evt.getButtonList());
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onActionPerformed(GuiScreenEvent.ActionPerformedEvent evt)
    {
        if (evt.getGui() instanceof GuiMainMenu)
        {
            GuiMainMenuHandler.onActionPerformed(evt.getButton());
        }
    }
}
