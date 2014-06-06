package com.dynious.versionchecker.handler;

import com.dynious.versionchecker.helper.ModHelper;
import com.dynious.versionchecker.lib.IMCOperations;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;

public class IMCHandler
{
    public static void processMessage(FMLInterModComms.IMCEvent event)
    {
        for (FMLInterModComms.IMCMessage message : event.getMessages())
        {
            if (message.key.equalsIgnoreCase(IMCOperations.ADD_UPDATE) && message.isNBTMessage())
            {
                LogHandler.info("Received update from mod " + message.getSender());
                processAddUpdateMessage(message.getNBTValue(), message.getSender());
            }
        }
    }

    public static void processAddUpdateMessage(NBTTagCompound tag, String sender)
    {
        Update update = new Update(sender);

        if (tag.hasKey(IMCOperations.MOD_DISPLAY_NAME))
        {
            update.displayName = tag.getString(IMCOperations.MOD_DISPLAY_NAME);
        }
        else
        {
            update.displayName = ModHelper.getModContainer(sender).getName();
        }

        if (tag.hasKey(IMCOperations.OLD_VERSION))
        {
            update.oldVersion = tag.getString(IMCOperations.OLD_VERSION);
        }
        else
        {
            update.oldVersion = ModHelper.getModContainer(sender).getVersion();
        }

        if (tag.hasKey(IMCOperations.NEW_VERSION))
        {
            update.newVersion = tag.getString(IMCOperations.NEW_VERSION);
        }

        if (tag.hasKey(IMCOperations.UPDATE_URL))
        {
            update.updateURL = tag.getString(IMCOperations.UPDATE_URL);
        }

        if (tag.hasKey(IMCOperations.IS_DIRECT_LINK))
        {
            update.isDirectLink = tag.getBoolean(IMCOperations.IS_DIRECT_LINK);
        }

        if (tag.hasKey(IMCOperations.CHANGE_LOG))
        {
            update.changeLog = tag.getString(IMCOperations.CHANGE_LOG);
        }

        if (tag.hasKey(IMCOperations.NEW_FILE_NAME))
        {
            update.changeLog = tag.getString(IMCOperations.CHANGE_LOG);
        }

        UpdateHandler.addUpdate(update);
    }
}
