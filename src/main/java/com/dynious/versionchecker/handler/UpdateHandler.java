package com.dynious.versionchecker.handler;

import com.dynious.versionchecker.api.Update;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UpdateHandler
{
    private static List<Update> updateList = new ArrayList<Update>();

    public static void addUpdate(Update update)
    {
        if (isUpdateBetter(update))
            updateList.add(update);
    }

    public static int getListSize()
    {
        return updateList.size();
    }

    public static Update getElement(int index)
    {
        if (index >= 0 && index < updateList.size())
        {
            return updateList.get(index);
        }
        return null;
    }

    public static boolean isUpdateBetter(Update update)
    {
        for (Iterator<Update> iterator = updateList.iterator(); iterator.hasNext(); )
        {
            Update update1 = iterator.next();
            if (update1.MOD_ID.equalsIgnoreCase(update.MOD_ID))
            {
                if (update.updateType.ordinal() < update1.updateType.ordinal())
                {
                    iterator.remove();
                    return true;
                }
                
                if (update.updateURL != null && !update.updateURL.isEmpty())
                {
                    if (update1.updateURL == null || update1.updateURL.isEmpty())
                    {
                        iterator.remove();
                        return true;
                    }
                    if (!update1.isDirectLink && update.isDirectLink)
                    {
                        iterator.remove();
                        return true;
                    }
                    if ((update1.changeLog == null || update1.changeLog.isEmpty()) && (update.changeLog != null && !update.changeLog.isEmpty()) && update.newVersion.equalsIgnoreCase(update1.newVersion))
                        update1.changeLog = update.changeLog;
                }
                return false;
            }
        }
        return true;
    }
}
