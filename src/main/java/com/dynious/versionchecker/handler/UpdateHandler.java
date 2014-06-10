package com.dynious.versionchecker.handler;

import com.dynious.versionchecker.api.Update;

import java.util.ArrayList;
import java.util.List;

public class UpdateHandler
{
    private static List<Update> updateList = new ArrayList<Update>();

    public static void addUpdate(Update update)
    {
        if (!hasMessageFrom(update.MOD_ID))
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

    public static boolean hasMessageFrom(String modid)
    {
        for (Update update1 : updateList)
        {
            if (update1.MOD_ID.equalsIgnoreCase(modid))
                return true;
        }
        return false;
    }
}
