package com.dynious.versionchecker.handler;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dynious.versionchecker.helper.DesktopHelper;
import com.dynious.versionchecker.helper.ModHelper;
import com.dynious.versionchecker.lib.Reference;

import sun.misc.URLClassPath;
import sun.net.util.URLUtil;

public class RemoveHandler
{
    public static final File DELETE_FILE = new File(DesktopHelper.MOD_FOLDER, "filesToDelete.txt");
    public static List<File> filesToDelete = new ArrayList<File>();

    public static void init()
    {
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());
    }

    @SuppressWarnings("unchecked")
    public static boolean deleteFile(File file)
    {
        if (file.delete())
        {
            return true;
        }

        try
        {
            ClassLoader cl = RemoveHandler.class.getClassLoader();
            URL url = file.toURI().toURL();
            Field f_ucp = URLClassLoader.class.getDeclaredField("ucp");
            Field f_loaders = URLClassPath.class.getDeclaredField("loaders");
            Field f_lmap = URLClassPath.class.getDeclaredField("lmap");
            f_ucp.setAccessible(true);
            f_loaders.setAccessible(true);
            f_lmap.setAccessible(true);

            URLClassPath ucp = (URLClassPath) f_ucp.get(cl);
            Closeable loader = ((Map<String, Closeable>) f_lmap.get(ucp)).remove(URLUtil.urlNoFragString(url));
            if (loader != null)
            {
                loader.close();
                ((List<?>) f_loaders.get(ucp)).remove(loader);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return file.delete();
    }

    public static class ShutDownThread extends Thread
    {
        @Override
        public void run()
        {
            System.gc();
            for (Iterator<File> iterator = RemoveHandler.filesToDelete.iterator(); iterator.hasNext(); )
            {
                File file = iterator.next();
                if (RemoveHandler.deleteFile(file))
                {
                    iterator.remove();
                }
            }
            if (!RemoveHandler.filesToDelete.isEmpty())
            {
                try
                {
                    FileWriter writer = new FileWriter(DELETE_FILE, false);
                    for (File file : RemoveHandler.filesToDelete)
                    {
                        writer.write(file.getAbsolutePath() + System.getProperty("line.separator"));
                    }
                    writer.close();

                    String jvm = new File(new File(System.getProperty("java.home"), "bin"), "java").getAbsolutePath();
                    String s = "\"" + jvm + "\"" + " -jar " + "\"" + ModHelper.getModContainer(Reference.MOD_ID).getSource().getAbsolutePath() + "\"" + " " + "\"" + DELETE_FILE.getAbsolutePath() + "\"";
                    Runtime.getRuntime().exec(s);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
