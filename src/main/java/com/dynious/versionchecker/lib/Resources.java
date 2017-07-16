package com.dynious.versionchecker.lib;

import net.minecraft.util.ResourceLocation;

public class Resources
{
    public static final String MOD_ID = Reference.MOD_ID.toLowerCase();
    public static final String GUI_SHEET_LOCATION = "textures/gui/";

    public static final ResourceLocation GUI_ICONS = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "icons.png");
    public static final ResourceLocation GUI_BUTTON_UPDATE = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "notify.png");
    public static final ResourceLocation GUI_LOGO = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "logo.png");
    public static final ResourceLocation GUI_WINDOW = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "window.png");
    public static final ResourceLocation GUI_BUTTON_TICK = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "tick_button.png");
    public static final ResourceLocation GUI_BUTTON_NEM = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "nem_button.png");
    public static final ResourceLocation GUI_BUTTON_CURSE = new ResourceLocation(MOD_ID, GUI_SHEET_LOCATION + "curse_button.png");
}
