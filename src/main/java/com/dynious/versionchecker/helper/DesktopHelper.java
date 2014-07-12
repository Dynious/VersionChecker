package com.dynious.versionchecker.helper;

import org.lwjgl.Sys;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DesktopHelper
{
    public static final File MOD_FOLDER;

    static
    {
        MOD_FOLDER = new File("mods");
    }
    public static void openFolderInExplorer(File file)
    {
        try
        {
            Desktop.getDesktop().browse(file.toURI());
        }
        catch (IOException e)
        {
            Sys.openURL("file://" + file.getAbsolutePath());
        }
    }
}
