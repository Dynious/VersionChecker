package com.dynious.versionchecker.handler;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpdateHandler
{
    private static List<Update> updateList = new ArrayList<Update>();

    public static void addUpdate(Update update)
    {
        for (Update update1 : updateList)
        {
            if (update1.MOD_ID.equalsIgnoreCase(update.MOD_ID))
                return;
        }
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
}
