package com.dynious.versionchecker.config;

import com.dynious.versionchecker.VersionChecker;
import com.dynious.versionchecker.lib.Reference;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler
{
    public static Configuration configFile;

    public static void init(FMLPreInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(new ConfigHandler());
        configFile = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.getModID().equals(Reference.MOD_ID))
            ConfigHandler.syncConfig();
    }

    public static void syncConfig()
    {
        VersionChecker.disableNEMCheck = getConfiguration("Disable NEM Check", VersionChecker.DISABLE_NEM_CHECK_DEFAULT,
                "Disable checking against the NotEnoughMods database.");

        if (configFile.hasChanged())
            configFile.save();
    }

    private static int getConfiguration(String setting, int defaultSetting, String comment)
    {
        return configFile.get(Configuration.CATEGORY_GENERAL, setting, defaultSetting, comment).getInt(defaultSetting);
    }

    private static boolean getConfiguration(String setting, boolean defaultSetting, String comment)
    {
        return configFile.get(Configuration.CATEGORY_GENERAL, setting, defaultSetting, comment).getBoolean(defaultSetting);
    }
}
