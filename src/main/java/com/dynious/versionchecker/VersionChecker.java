package com.dynious.versionchecker;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.checker.UpdateChecker;
import com.dynious.versionchecker.event.EventHandler;
import com.dynious.versionchecker.handler.IMCHandler;
import com.dynious.versionchecker.handler.LogHandler;
import com.dynious.versionchecker.handler.RemoveHandler;
import com.dynious.versionchecker.handler.UpdateHandler;
import com.dynious.versionchecker.lib.Reference;
import com.dynious.versionchecker.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

        //sendABunchOfDerpyMessages();
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
    
    public void sendABunchOfDerpyMessages()
    {
        for (int i = 0; i < 50; i++)
        {
            Update update = new Update("Test" + i);
            update.displayName = "Test" + i;
            update.oldVersion = "0.0.0.0.0.0.0";
            update.newVersion = "9.9.9.9.9.9.9";
            StringBuilder s = new StringBuilder();
            for (int a = 0; a < 50; a++)
            {
                s.append("bla");
                for (int o = 0; o < a; o++)
                {
                    s.append("a");
                }
                s.append("\n");
            }
            update.changeLog = s.toString();
            UpdateHandler.addUpdate(update);
        }
    }
}
