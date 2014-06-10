package com.dynious.versionchecker;

import com.dynious.versionchecker.checker.UpdateChecker;
import com.dynious.versionchecker.event.EventHandler;
import com.dynious.versionchecker.handler.IMCHandler;
import com.dynious.versionchecker.handler.RemoveHandler;
import com.dynious.versionchecker.lib.Reference;
import com.dynious.versionchecker.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class VersionChecker
{
    @Mod.Instance(Reference.MOD_ID)
    public static VersionChecker instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    public static final String REMOTE_VERSION_URL = "https://raw.github.com/Dynious/VersionChecker/master/version.json";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        RemoveHandler.init();

        FMLInterModComms.sendRuntimeMessage(Reference.MOD_ID, "VersionChecker", "addVersionCheck", REMOTE_VERSION_URL);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        UpdateChecker.execute();
    }

    @Mod.EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event)
    {
        IMCHandler.processMessages(event.getMessages());
    }
}
