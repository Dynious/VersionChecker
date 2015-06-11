package com.dynious.versionchecker.helper;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.handler.LogHandler;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.logging.Logger;

public class WebHelper {
    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                desktop.browse(uri);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "seamonkey", "galeon", "kazehakase", "mozilla", "netscape", "google-chrome", "google-chrome-stable"};
            String osName = System.getProperty("os.name");
            try
            {
                if (osName.contains("nix") || osName.contains("nux"))
                {
                    boolean found = false;
                    for (String browser : browsers)
                        if (!found)
                        {
                            found = Runtime.getRuntime().exec(new String[]{"which", browser}).waitFor() == 0;
                            if (found)
                            {
                                Runtime.getRuntime().exec(new String[]{browser, uri.toURL().toString()});
                            }
                        }
                    if (!found)
                    {
                        throw new Exception(Arrays.toString(browsers));
                    }
                }
            } catch (Exception e)
            {
                Logger.getGlobal().severe("Could not open URI properly, please report your browser and your OS to the Version Checker Github!");
            }

        }
    }

    public static void openWebpage(URL url) {
        try
        {
            openWebpage(url.toURI());
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public static void openWebpage(String string) {
        try
        {
            openWebpage(new URI(string));
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public static boolean downloadUpdate(Update update) {
        ModContainer mod = ModHelper.getModContainer(update.MOD_ID);
        if (mod != null && mod.getSource() != null && mod.getSource().isFile())
        {
            String fileName = "";
            if (update.newFileName != null && !update.newFileName.isEmpty())
            {
                fileName = mod.getSource().getParent() + File.separator + update.newFileName;
            }
            else
            {
                fileName = mod.getSource().getAbsolutePath();
                String newFileName = fileName.replace(update.oldVersion, update.newVersion);
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
                {
                    return true;
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static File downloadFileFromURL(URL url, Update update, ModContainer mod, String fileName) throws IOException {
        File newFile = new File(fileName);
        FileUtils.copyURLToFile(url, newFile);
        return newFile;
    }

    public static String getLatestFilenameFromCurse(String urlString) {
        try
        {
            while (urlString != null && !urlString.isEmpty())
            {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(false);
                urlString = connection.getHeaderField("Location");

                if (urlString != null && (urlString.endsWith(".jar") || urlString.endsWith(".zip")))
                {
                    return urlString.substring(urlString.lastIndexOf("/") + 1);
                }
            }
        } catch (MalformedURLException e)
        {
            LogHandler.error("Malformed URL was given when searching in Curse database!");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
