package com.dynious.versionchecker.checker;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.handler.UpdateHandler;
import com.dynious.versionchecker.helper.ModHelper;
import com.dynious.versionchecker.lib.Strings;
import com.google.gson.Gson;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.util.StatCollector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NEMChecker implements Runnable
{
    private static NEMChecker instance = new NEMChecker();

    private final Gson gson = new Gson();

    public static String getNemMcVersion()
    {
        return Loader.instance().getMCVersionString().substring(10);
    }

    @Override
    public void run()
    {
        InputStream inputStream = null;
        Scanner s = null;
        try
        {
            URL url = new URL("http://bot.notenoughmods.com/" + getNemMcVersion() + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            inputStream = (InputStream) conn.getContent();
            s = new Scanner(inputStream).useDelimiter("\\A");
            String string = s.next();
            NEMModInfo[] mods = gson.fromJson(string, NEMModInfo[].class);

            for (NEMModInfo mod : mods)
            {
                ModContainer container = ModHelper.getModContainer(mod.getModid());
                if (container != null)
                {
                    if (!mod.isUpToDate(container.getVersion(), container.getDisplayVersion()))
                    {
                        Update update = new Update(container.getModId());
                        update.displayName = container.getName();
                        update.oldVersion = mod.getActualModVersion() != null ? mod.getActualModVersion() : NEMUtils.patchVersion(mod.getVersion());
                        update.newVersion = mod.getVersion();
                        update.changeLog = StatCollector.translateToLocal(Strings.NEM_UPDATE);

                        update.isDirectLink = false;
                        if (mod.getLongurl() != null && !mod.getLongurl().isEmpty())
                        {
                            update.updateURL = mod.getLongurl();
                        }
                        update.updateType = Update.UpdateType.NOT_ENOUGH_MODS;
                        UpdateHandler.addUpdate(update);
                    }
                }
            }
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                s.close();
                inputStream.close();
            } catch (Throwable e2)
            {
            }
        }
    }

    public static void execute()
    {
        new Thread(instance).start();
    }
}
