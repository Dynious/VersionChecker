package com.dynious.versionchecker;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.checker.NEMChecker;
import com.dynious.versionchecker.checker.UpdateChecker;
import com.dynious.versionchecker.config.ConfigHandler;
import com.dynious.versionchecker.event.EventHandler;
import com.dynious.versionchecker.handler.IMCHandler;
import com.dynious.versionchecker.handler.RemoveHandler;
import com.dynious.versionchecker.handler.UpdateHandler;
import com.dynious.versionchecker.lib.Reference;
import com.dynious.versionchecker.proxy.CommonProxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, guiFactory = Reference.CONFIG_GUI_FACTORY)
public class VersionChecker
{
    @Mod.Instance(Reference.MOD_ID)
    public static VersionChecker instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    public static final String REMOTE_VERSION_URL = "https://raw.github.com/Dynious/VersionChecker/master/version.json";
    public static final boolean DISABLE_NEM_CHECK_DEFAULT = false;
    public static boolean disableNEMCheck;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        FMLCommonHandler.instance().bus().register(new ConfigHandler());

        ConfigHandler.init(event);

        RemoveHandler.init();

        FMLInterModComms.sendRuntimeMessage(Reference.MOD_ID, "VersionChecker", "addVersionCheck", REMOTE_VERSION_URL);

        if (!disableNEMCheck)
            NEMChecker.execute();

        /*
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("curseProjectName", "221140-version-checker");
        compound.setString("curseFilenameParser", "VersionChecker-[].jar");

        FMLInterModComms.sendRuntimeMessage(Reference.MOD_ID, "VersionChecker", "addCurseCheck", compound);
        */
        
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
