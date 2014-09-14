package com.dynious.versionchecker.checker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NEMModInfo
{
    private String name;
    private String version;
    private String longurl;
    private String shorturl;
    private String[] aliases;
    private String comment;
    private String modid;
    private String dev;
    private String actualModVersion;

    public boolean isUpToDate(String modVersion, String displayVersion)
    {
        if (modVersion.equals("%VERSION%") || displayVersion.equals("%VERSION%"))
        {
            return false;
        }

        if (NEMUtils.isDevVersion(version))
        {
            if (dev == null || dev.isEmpty())
                return true;

            version = dev;
        }

        modVersion = NEMUtils.patchVersion(modVersion);
        displayVersion = NEMUtils.patchVersion(displayVersion);

        Pattern versionPattern = Pattern.compile(NEMUtils.allVersionRegex);
        Matcher matcher;
        boolean modVersionValid;
        if (modVersion.equals(displayVersion))
        {
            actualModVersion = modVersion;
            matcher = versionPattern.matcher(actualModVersion);
            modVersionValid = matcher.matches();
        }
        else
        {
            matcher = versionPattern.matcher(modVersion);
            modVersionValid = matcher.matches();
            matcher = versionPattern.matcher(displayVersion);
            boolean displayModVersionValid = matcher.matches();
            if (modVersionValid && displayModVersionValid)
            {
                // Take newer or more detailed version
                actualModVersion = (NEMUtils.isNewer(modVersion, displayVersion) ? modVersion : displayVersion);
            }
            else if (modVersionValid)
            {
                actualModVersion = modVersion;
            }
            else if (displayModVersionValid)
            {
                actualModVersion = displayVersion;
                modVersionValid = true;
            }
            else
            {
                actualModVersion = modVersion + "/" + displayVersion;
            }
        }

        return modVersionValid && isUpToDate(actualModVersion);
    }

    private boolean isUpToDate(String modVersion)
    {
        Pattern versionPattern = Pattern.compile(NEMUtils.allVersionRegex);
        Matcher matcher;
        matcher = versionPattern.matcher(modVersion);
        boolean modVersionValid = matcher.matches();

        return modVersionValid && !NEMUtils.isNewer(version, modVersion);
    }

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public String getLongurl()
    {
        return longurl;
    }

    public String shorturl()
    {
        return shorturl;
    }

    public String[] getAliases()
    {
        return aliases;
    }

    public String getComment()
    {
        return comment;
    }

    public String getModid()
    {
        return modid;
    }

    public String getActualModVersion()
    {
        return actualModVersion;
    }
}
