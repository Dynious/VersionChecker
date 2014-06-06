package com.dynious.versionchecker.lib;

import net.minecraft.util.ResourceLocation;

public class Resources
{
    public static final String MOD_ID = Reference.MOD_ID.toLowerCase();
    public static final String GUI_SHEET_LOCATION = "textures/gui/";

    public static final ResourceLocation GUI_ICONS = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "icons.png");
    public static final ResourceLocation GUI_BUTTON_UPDATE = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "notify.png");
}
