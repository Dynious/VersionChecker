package com.dynious.versionchecker;

import com.dynious.versionchecker.event.EventHandler;
import com.dynious.versionchecker.handler.IMCHandler;
import com.dynious.versionchecker.handler.RemoveHandler;
import com.dynious.versionchecker.lib.Reference;
import com.dynious.versionchecker.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        RemoveHandler.init();
    }

    @Mod.EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event)
    {
        IMCHandler.processMessage(event);
    }
}
