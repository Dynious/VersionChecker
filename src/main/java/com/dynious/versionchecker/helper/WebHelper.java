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

    public static void downloadUpdate(Update update)
    {
        ModContainer mod = ModHelper.getModContainer(update.MOD_ID);
        if (mod != null)
        {
            String fileName = "";
            if (update.newFileName != null)
            {
                fileName = update.newFileName;
            }
            else
            {
                fileName = mod.getSource().getAbsolutePath();
                fileName = fileName.replaceAll(update.oldVersion, update.newVersion);
            }
            try
            {
                URL url = new URL(update.updateURL);
                downloadFileFromURL(url, update, mod, fileName);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void downloadFileFromURL(URL url, Update update, ModContainer mod, String fileName)
    {
        File newFile = new File(fileName);
        try
        {
            FileUtils.copyURLToFile(url, newFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        RemoveHandler.filesToDelete.add(mod.getSource());
    }
}
