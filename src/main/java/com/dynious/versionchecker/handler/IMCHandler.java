package com.dynious.versionchecker.handler;

import com.dynious.versionchecker.api.Update;
import com.dynious.versionchecker.checker.UpdateChecker;
import com.dynious.versionchecker.helper.ModHelper;
import com.dynious.versionchecker.helper.WebHelper;
import com.dynious.versionchecker.lib.IMCOperations;
import com.dynious.versionchecker.lib.Strings;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class IMCHandler
{
    public static void processMessages(List<FMLInterModComms.IMCMessage> messageList)
    {
        for (FMLInterModComms.IMCMessage message : messageList)
        {
            if (message.key.equalsIgnoreCase(IMCOperations.ADD_UPDATE))
            {
                LogHandler.info("Received update from mod " + message.getSender());
                if (message.isNBTMessage())
                {
                    processAddUpdateMessage(message.getNBTValue(), message.getSender());
                }
                else if (message.isStringMessage())
                {
                    processAddUpdateMessage(message.getStringValue(), message.getSender());
                }
            }
            else if (message.key.equalsIgnoreCase(IMCOperations.ADD_VERSION_CHECK))
            {
                LogHandler.info("Received version check from mod " + message.getSender());
                if (message.isStringMessage())
                {
                    processVersionCheckMessage(message.getSender(), message.getStringValue());
                }
            }
            else if (message.key.equalsIgnoreCase(IMCOperations.ADD_CURSE_CHECK))
            {
                LogHandler.info("Received curse check from mod " + message.getSender());
                if (message.isNBTMessage())
                {
                    processCurseCheckMessage(message.getSender(), message.getNBTValue());
                }
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
            update.newFileName = tag.getString(IMCOperations.NEW_FILE_NAME);
        }

        UpdateHandler.addUpdate(update);
    }

    public static void processAddUpdateMessage(String string, String sender)
    {
        Update update = Update.createFromJson(string);
        if (update != null)
        {
            if (update.displayName == null)
            {
                update.displayName = ModHelper.getModContainer(sender).getName();
            }
            if (update.oldVersion == null)
            {
                update.oldVersion = ModHelper.getModContainer(sender).getVersion();
            }
            UpdateHandler.addUpdate(update);
        }
    }

    public static void processVersionCheckMessage(String sender, String url)
    {
        UpdateChecker.addModToCheck(sender, url);
    }

    public static void processCurseCheckMessage(String sender, NBTTagCompound tag)
    {
        if (tag.hasKey(IMCOperations.CURSE_PROJECT_NAME) && tag.hasKey(IMCOperations.CURSE_FILENAME_PARSER))
        {
            String curseProjectName = tag.getString(IMCOperations.CURSE_PROJECT_NAME);
            String latestFilename = WebHelper.getLatestFilenameFromCurse("http://minecraft.curseforge.com/projects/" + curseProjectName + "/files/latest");
            if (latestFilename != null)
            {
                String fileNameParser = tag.getString(IMCOperations.CURSE_FILENAME_PARSER);
                int i = fileNameParser.indexOf('[');
                int o = fileNameParser.indexOf(']');
                if (i != -1 && o != -1)
                {
                    String version = latestFilename.replace(fileNameParser.substring(0, i), "").replace(fileNameParser.substring(o + 1, fileNameParser.length()), "");

                    if (version.equals(tag.hasKey(IMCOperations.OLD_VERSION) ? tag.getString(IMCOperations.OLD_VERSION) : ModHelper.getModContainer(sender).getVersion()))
                        return;

                    Update update = new Update(sender);

                    update.newVersion = version;
                    update.updateURL = "http://minecraft.curseforge.com/projects/" + curseProjectName + "/files/latest";
                    update.isDirectLink = true;
                    update.newFileName = latestFilename;
                    update.changeLog = I18n.translateToLocal(Strings.CURSE_UPDATE);

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

                    update.updateType = Update.UpdateType.CURSE;

                    UpdateHandler.addUpdate(update);
                    return;
                }
            }
        }
        LogHandler.error(String.format("An error was encountered when fetching latest version from Curse for %s!", sender));
    }
}
