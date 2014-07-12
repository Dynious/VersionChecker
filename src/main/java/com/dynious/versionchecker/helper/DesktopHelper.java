package com.dynious.versionchecker.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class DesktopHelper
{
    public static final File MOD_FOLDER;
    private static final Logger logger = LogManager.getLogger();


    static
    {
        MOD_FOLDER = new File(Minecraft.getMinecraft().mcDataDir, "mods");
    }
    public static void openFolderInExplorer(File file)
    {
        String s = file.getAbsolutePath();

        if (Util.getOSType() == Util.EnumOS.MACOS)
        {
            try
            {
                logger.info(s);
                Runtime.getRuntime().exec(new String[] {"/usr/bin/open", s});
                return;
            }
            catch (IOException e)
            {
                logger.error("Couldn\'t open file", e);
            }
        }
        else if (Util.getOSType() == Util.EnumOS.WINDOWS)
        {
            String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] {s});

            try
            {
                Runtime.getRuntime().exec(s1);
                return;
            }
            catch (IOException ioexception)
            {
                logger.error("Couldn\'t open file", ioexception);
            }
        }

        boolean flag = false;

        try
        {
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {file.toURI()});
        }
        catch (Throwable throwable)
        {
            logger.error("Couldn\'t open link", throwable);
            flag = true;
        }

        if (flag)
        {
            logger.info("Opening via system class!");
            Sys.openURL("file://" + s);
        }
    }
}
