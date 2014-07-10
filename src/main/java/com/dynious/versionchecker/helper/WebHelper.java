package com.dynious.versionchecker.helper;

import com.dynious.versionchecker.handler.RemoveHandler;
import com.dynious.versionchecker.api.Update;
import cpw.mods.fml.common.ModContainer;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebHelper
{
    public static void openWebpage(URI uri)
    {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                desktop.browse(uri);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void openWebpage(URL url)
    {
        try
        {
            openWebpage(url.toURI());
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public static void openWebpage(String string)
    {
        try
        {
            openWebpage(new URI(string));
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean downloadUpdate(Update update)
    {
        ModContainer mod = ModHelper.getModContainer(update.MOD_ID);
        if (mod != null)
        {
            String fileName = "";
            if (update.newFileName != null && !update.newFileName.isEmpty())
            {
                fileName = mod.getSource().getParent() + File.separator + update.newFileName;
            }
            else
            {
                fileName = mod.getSource().getAbsolutePath();
                String newFileName = fileName.replaceAll(update.oldVersion, update.newVersion);
                if (fileName.equalsIgnoreCase(newFileName))
                {
                    int i = newFileName.lastIndexOf(".");
                    if (i == -1)
                    {
                        newFileName += "-new";
                    }
                    else
                    {
                        newFileName = newFileName.substring(0, i) + "-new" + newFileName.substring(i);
                    }
                }
                fileName = newFileName;
            }
            try
            {
                URL url = new URL(update.updateURL);
                File file = downloadFileFromURL(url, update, mod, fileName);
                if (file != null && file.exists() && file.length() > 0)
                    return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static File downloadFileFromURL(URL url, Update update, ModContainer mod, String fileName) throws IOException
    {
        File newFile = new File(fileName);
        FileUtils.copyURLToFile(url, newFile);
        return newFile;
    }
}
