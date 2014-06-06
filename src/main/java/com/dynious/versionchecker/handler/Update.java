package com.dynious.versionchecker.handler;

public class Update
{
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

    public void hasBeenDownloaded()
    {
        this.isDownloaded = true;
    }
}
