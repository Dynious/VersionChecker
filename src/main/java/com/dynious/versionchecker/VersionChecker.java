package com.dynious.versionchecker;

import com.dynious.versionchecker.event.EventHandler;
import com.dynious.versionchecker.handler.IMCHandler;
import com.dynious.versionchecker.handler.RemoveHandler;
import com.dynious.versionchecker.lib.IMCOperations;
import com.dynious.versionchecker.lib.Reference;
import com.dynious.versionchecker.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.nbt.NBTTagCompound;
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

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString(IMCOperations.OLD_VERSION, "1.3.3.7");
        tag.setString(IMCOperations.NEW_VERSION, "1.3.3.8");
        tag.setString(IMCOperations.MOD_DISPLAY_NAME, "A Funky Mod!");
        tag.setString(IMCOperations.UPDATE_URL, "http://www.google.com");
        tag.setBoolean(IMCOperations.IS_DIRECT_LINK, false);
        FMLInterModComms.sendMessage(Reference.MOD_ID, IMCOperations.ADD_UPDATE, tag);
    }

    @Mod.EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event)
    {
        IMCHandler.processMessage(event);
    }
}
