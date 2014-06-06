package com.dynious.versionchecker.handler;

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
        WebHelper.downloadUpdate(update);
        downloadingUpdates.remove(update);
        update.hasBeenDownloaded();
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
