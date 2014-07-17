package com.dynious.versionchecker.api;

import com.dynious.versionchecker.helper.MatchHelper;

import java.util.List;

public class VersionContainer
{
    public List<Version> versionList;

    public VersionContainer(List<Version> versionList)
    {
        this.versionList = versionList;
    }

    public Version getLatestFromMcVersion(String McVersion)
    {
        for (Version version : versionList)
        {
            if (MatchHelper.doStringsMatch(McVersion, version.getMcVersion()))
            {
                return version;
            }
        }
        return null;
    }

    public static class Version
    {
        private String mcVersion;
        private String modVersion;
        private List<String> changeLog;
        private String updateURL;
        private boolean isDirectLink;
        private String newFileName;

        private Version(String mcVersion, String modVersion, List<String> changeLog, String updateURL, boolean isDirectLink, String newFileName)
        {
            this.mcVersion = mcVersion;
            this.modVersion = modVersion;
            this.changeLog = changeLog;
            this.updateURL = updateURL;
            this.isDirectLink = isDirectLink;
            this.newFileName = newFileName;
        }

        public String getMcVersion()
        {
            return mcVersion;
        }

        public String getModVersion()
        {
            return modVersion;
        }

        public List<String> getChangeLog()
        {
            return changeLog;
        }

        public String getUpdateURL()
        {
            return updateURL;
        }

        public boolean isDirectLink()
        {
            return isDirectLink;
        }

        public String getNewFileName()
        {
            return newFileName;
        }
    }
}
