package com.dynious.versionchecker.api;

import com.dynious.versionchecker.handler.LogHandler;
import com.google.gson.*;

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

    private boolean isDownloaded;

    public Update(String modId)
    {
        this.MOD_ID = modId;
    }

    public boolean isDownloaded()
    {
        return isDownloaded;
    }

    public void setDownloaded()
    {
        this.isDownloaded = true;
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
}
