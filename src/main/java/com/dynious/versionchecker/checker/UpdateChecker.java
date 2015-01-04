package com.dynious.versionchecker.checker;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.api.VersionContainer;
import com.dynious.versionchecker.handler.IMCHandler;
import com.dynious.versionchecker.handler.LogHandler;
import com.dynious.versionchecker.handler.UpdateHandler;
import com.dynious.versionchecker.helper.ModHelper;
import com.dynious.versionchecker.lib.Reference;
import com.google.gson.Gson;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateChecker implements Runnable
{
    private static UpdateChecker instance = new UpdateChecker();

    private static Map<ModContainer, String> modsToCheck = new HashMap<ModContainer, String>();

    private static Gson gson = new Gson();

    private static final int VERSION_CHECK_ATTEMPTS = 3;

    public static enum CheckState
    {
        UNINITIALIZED,
        CURRENT,
        OUTDATED,
        ERROR,
        MC_VERSION_NOT_FOUND
    }

    public static void addModToCheck(String modId, String url)
    {
        addModToCheck(ModHelper.getModContainer(modId), url);
    }

    public static void addModToCheck(ModContainer mod, String url)
    {
        if (mod == null || url == null || url.isEmpty() || modsToCheck.keySet().contains(mod))
            return;

        modsToCheck.put(mod, url);
    }

    public static void checkVersion()
    {
        CheckState result;
        VersionContainer.Version latest;

        for (Iterator<Map.Entry<ModContainer, String>> iterator = modsToCheck.entrySet().iterator(); iterator.hasNext(); )
        {
            Map.Entry<ModContainer, String> entry = iterator.next();
            result = CheckState.UNINITIALIZED;
            latest = null;

            try
            {
                String json = IOUtils.toString(new URL(entry.getValue()));

                VersionContainer versionContainer = gson.fromJson(json, VersionContainer.class);
                latest = versionContainer.getLatestFromMcVersion(Loader.instance().getMCVersionString());

                if (latest != null)
                {
                    if (latest.getModVersion().equalsIgnoreCase(entry.getKey().getVersion()))
                    {
                        result = CheckState.CURRENT;
                        iterator.remove();
                    }
                    else
                    {
                        result = CheckState.OUTDATED;
                        addUpdateToList(entry.getKey(), latest);
                        iterator.remove();
                    }
                }
                else
                {
                    result = CheckState.MC_VERSION_NOT_FOUND;
                    iterator.remove();
                }
            } catch (Exception ignored)
            {
            } finally
            {
                if (result == CheckState.UNINITIALIZED)
                {
                    result = CheckState.ERROR;
                }
                logResult(entry.getKey(), result, latest);
            }
        }
    }

    public static void logResult(ModContainer mod, CheckState result, VersionContainer.Version version)
    {
        if (result == CheckState.CURRENT || result == CheckState.OUTDATED)
        {
            LogHandler.info(getResultMessage(mod, result, version));
        }
        else
        {
            LogHandler.warning(getResultMessage(mod, result, version));
        }
    }

    public static String getResultMessage(ModContainer mod, CheckState result, VersionContainer.Version version)
    {
        if (result == CheckState.UNINITIALIZED)
        {
            return String.format("Version Checker Status for %s: UNINITIALIZED", mod.getName());
        }
        else if (result == CheckState.CURRENT)
        {
            return String.format("Version Checker Status for %s: CURRENT", mod.getName());
        }
        else if (result == CheckState.OUTDATED && version != null)
        {
            return String.format("Version Checker Status for %s: OUTDATED! Using %s, latest %s", mod.getName(), mod.getVersion(), version.getModVersion());
        }
        else if (result == CheckState.ERROR)
        {
            return String.format("Version Checker Status for %s: ERROR", mod.getName());
        }
        else if (result == CheckState.MC_VERSION_NOT_FOUND)
        {
            return String.format("Version Checker Status for %s: MC VERSION NOT SUPPORTED", mod.getName());
        }
        else
        {
            return String.format("Version Checker Status for %s: ERROR", mod.getName());
        }
    }

    public static void addUpdateToList(ModContainer mod, VersionContainer.Version version)
    {
        Update update = new Update(mod.getModId());
        update.displayName = mod.getName();
        update.oldVersion = mod.getVersion();
        update.newVersion = version.getModVersion();

        if (version.getUpdateURL() != null && !version.getUpdateURL().isEmpty())
        {
            update.updateURL = version.getUpdateURL();
        }
        update.isDirectLink = version.isDirectLink();

        if (!version.getChangeLog().isEmpty())
        {
            StringBuilder builder = new StringBuilder();
            for (String changeLogLine : version.getChangeLog())
            {
                builder.append(changeLogLine).append("\n");
            }
            update.changeLog = builder.toString();
        }

        if (version.getNewFileName() != null && !version.getNewFileName().isEmpty())
        {
            update.newFileName = version.getNewFileName();
        }

        UpdateHandler.addUpdate(update);
    }

    @Override
    public void run()
    {
        int count = 0;

        try
        {
            while (count < VERSION_CHECK_ATTEMPTS - 1 && (count == 0 || !modsToCheck.isEmpty()))
            {
                IMCHandler.processMessages(FMLInterModComms.fetchRuntimeMessages(Reference.MOD_ID));
                checkVersion();
                count++;

                Thread.sleep(10000);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void execute()
    {
        new Thread(instance).start();
    }
}
