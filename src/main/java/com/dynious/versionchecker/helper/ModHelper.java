package com.dynious.versionchecker.helper;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

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
