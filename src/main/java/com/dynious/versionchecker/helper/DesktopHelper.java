package com.dynious.versionchecker.helper;

import org.lwjgl.Sys;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class DesktopHelper {
    public static final File MOD_FOLDER;

    static
    {
        MOD_FOLDER = new File("mods");
    }

    public static void openFolderInExplorer(File file) {
        String[] fileBrowsers = {"gentoo", "xfe", "nautilus", "dolphin", "andromeda", "pcmanfm", "spacefm", "thunar", "caja", "dino", "xdg-open"};
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e)
            {
                Sys.openURL("file://" + file.getAbsolutePath());
            }
        }
        else
        {
            String osName = System.getProperty("os.name");
            try
            {
                if (osName.contains("nix") || osName.contains("nux"))
                {
                    boolean found = false;
                    for (String fileBrowser : fileBrowsers)
                        if (!found)
                        {
                            found = Runtime.getRuntime().exec(new String[]{"which", fileBrowser}).waitFor() == 0;
                            if (found)
                            {
                                Runtime.getRuntime().exec(new String[]{fileBrowser, file.getAbsolutePath()});
                            }
                        }
                    if (!found)
                    {
                        throw new Exception(Arrays.toString(fileBrowsers));
                    }
                }
            } catch (Exception e)
            {
                Logger.getGlobal().severe("Could not open File Browser properly, please report your file browser and your OS to the Version Checker Github!");
            }
        }
    }
}
