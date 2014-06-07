package com.dynious.versionchecker.helper;

import com.dynious.versionchecker.lib.Reference;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DesktopHelper
{
    public static final File MOD_FOLDER;

    static
    {
        File file = ModHelper.getModContainer(Reference.MOD_ID).getSource();
        MOD_FOLDER = file.getParentFile();
    }
    public static void openFolderInExplorer(File file)
    {
        try
        {
            Desktop.getDesktop().open(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
