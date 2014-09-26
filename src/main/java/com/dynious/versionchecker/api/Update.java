package com.dynious.versionchecker.api;

import com.dynious.versionchecker.handler.LogHandler;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class Update
{
    private static final Gson gsonSerializer = new Gson();

    public final String MOD_ID;
    public String displayName;
    public String oldVersion;
    public String newVersion;
    public String updateURL;
    public boolean isDirectLink = true;
    public String newFileName;
    public String changeLog;
    public UpdateType updateType = UpdateType.NORMAL;

    private boolean isDownloaded;
    private boolean isErrored;

    public Update(String modId)
    {
        this.MOD_ID = modId;
    }

    public boolean isDownloaded()
    {
        return isDownloaded;
    }

    public boolean isErrored()
    {
        return isErrored;
    }

    public void setDownloaded(boolean downloaded)
    {
        this.isDownloaded = downloaded;
    }

    public void setErrored()
    {
        isErrored = true;
    }

    public static Update createFromJson(String jsonRecipeMapping)
    {
        try
        {
            return gsonSerializer.fromJson(jsonRecipeMapping, Update.class);
        }
        catch (JsonSyntaxException exception)
        {
            LogHandler.error(exception.getMessage());
        }
        catch (JsonParseException exception)
        {
            LogHandler.error(exception.getMessage());
        }

        return null;
    }

    public String toJson()
    {
        return gsonSerializer.toJson(this);
    }

    public static enum UpdateType
    {
        NORMAL,
        CURSE,
        NOT_ENOUGH_MODS
    }
}
