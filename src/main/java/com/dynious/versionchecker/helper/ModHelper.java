package com.dynious.versionchecker.helper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ModHelper
{
    public static ModContainer getModContainer(String modId)
    {
        for (ModContainer mod : Loader.instance().getActiveModList())
        {
            if (mod.getModId().equalsIgnoreCase(modId))
            {
                return mod;
            }
        }
        return null;
    }
}
