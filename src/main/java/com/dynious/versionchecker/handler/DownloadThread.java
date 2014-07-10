package com.dynious.versionchecker.handler;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.helper.ModHelper;
import com.dynious.versionchecker.helper.WebHelper;

import java.util.ArrayList;
import java.util.List;

public class DownloadThread implements Runnable
{
    private static List<Update> downloadingUpdates = new ArrayList<Update>();

    private Update update;

    private DownloadThread(Update update)
    {
        this.update = update;
    }

    @Override
    public void run()
    {
        boolean success = WebHelper.downloadUpdate(update);
        downloadingUpdates.remove(update);
        if (success)
        {
            RemoveHandler.filesToDelete.add(ModHelper.getModContainer(update.MOD_ID).getSource());
            update.setDownloaded(true);
        }
        else
        {
            update.setErrored();
        }
    }

    public static void downloadUpdate(Update update)
    {
        new Thread(new DownloadThread(update)).start();
        downloadingUpdates.add(update);
    }

    public static boolean isUpdating()
    {
        return !downloadingUpdates.isEmpty();
    }

    public static boolean isUpdating(Update update)
    {
        return downloadingUpdates.contains(update);
    }
}
