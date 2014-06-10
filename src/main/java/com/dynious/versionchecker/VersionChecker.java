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
        tag.setString("modDisplayName", Reference.NAME);
        tag.setString("oldVersion", Reference.VERSION);
        tag.setString("newVersion", "NEW!");
        tag.setString("updateUrl", "nonono");
        tag.setBoolean("isDirectLink", false);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 50; i++)
        {
            s.append("bla\n");
        }
        tag.setString("changeLog", s.toString());
        FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag);
    }

    @Mod.EventHandler
    public void handleIMCMessages(FMLInterModComms.IMCEvent event)
    {
        IMCHandler.processMessages(event.getMessages());
    }
}
