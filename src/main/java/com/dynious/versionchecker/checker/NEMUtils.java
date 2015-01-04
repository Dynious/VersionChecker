package com.dynious.versionchecker.checker;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NEMUtils
{
    public static final String versionDelimiters = "[\\.\\-_]";

    public static final String allVersionRegex = "[a-zA-Z0-9]+(" + versionDelimiters + "[a-zA-Z0-9]+)*";
    public static final String numericalVersionRegex = "[0-9]+(" + versionDelimiters + "[0-9]+)*";

    public static boolean isNumericalVersion(String version) {
        Pattern numVersionPattern = Pattern.compile(numericalVersionRegex);
        Matcher matcher = numVersionPattern.matcher(version);
        return matcher.matches();
    }

    public static String[] versionSplitting(String version, String regexVersionDelimiters) {
        Pattern pattern = Pattern.compile(regexVersionDelimiters);
        return pattern.split(version);
    }

    public static boolean isNewer(String version, String compareVersion) {
        if (version.equals(compareVersion))
            return false;

        boolean numericalCompare = isNumericalVersion(version) && isNumericalVersion(compareVersion);

        String[] versionParts = versionSplitting(version, versionDelimiters);
        String[] compareVersionParts = versionSplitting(compareVersion, versionDelimiters);

        // ignore versions with different numbers of tokens
        // because they are false positives more often than not
        if (versionParts.length != compareVersionParts.length)
            return false;

        if(numericalCompare) {
            for(int i = 0; i < Math.min(versionParts.length, compareVersionParts.length); i++) {
                Integer currentVersionNumber = Integer.parseInt(versionParts[i]);
                Integer currentCompareNumber = Integer.parseInt(compareVersionParts[i]);

                if(currentVersionNumber > currentCompareNumber) {
                    return true;
                } else if(currentVersionNumber < currentCompareNumber) {
                    return false;
                }
            }
            return false;
        } else {
            for(int i = 0; i < Math.min(versionParts.length, compareVersionParts.length); i++) {
                String currentVersionPart = versionParts[i];
                String currentComparePart = compareVersionParts[i];
                for(int c = 0; c < Math.min(currentVersionPart.length(), currentComparePart.length()); c++) {
                    if(currentVersionPart.charAt(c) > currentComparePart.charAt(c)) {
                        return true;
                    } else if(currentVersionPart.charAt(c) < currentComparePart.charAt(c)) {
                        return false;
                    }
                }
                if(currentVersionPart.length() > currentComparePart.length()) {
                    return true;
                } else if(currentVersionPart.length() < currentComparePart.length()) {
                    return false;
                }
            }
            return false;
        }
    }

    public static String patchVersion(String modVersion) {
        modVersion = modVersion.replaceAll(Pattern.quote("(" + NEMChecker.getMcVersion() + ")"), "");
        modVersion = modVersion.replaceAll(Pattern.quote("[" + NEMChecker.getMcVersion() + "]"), "");
        modVersion = modVersion.replaceAll("[\\)\\]]", "").replaceAll("[\\(\\[]", ".");
        modVersion = modVersion.replaceAll(Pattern.quote("_" + NEMChecker.getMcVersion()), "");
        modVersion = modVersion.replaceAll(Pattern.quote(NEMChecker.getMcVersion() + "_"), "");
        modVersion = modVersion.replaceAll(Pattern.quote(NEMChecker.getMcVersion() + "-"), "");
        modVersion = modVersion.replaceAll("^v", "").replaceAll("^V", "");
        modVersion = modVersion.replaceAll(" build ", ".").replaceAll("\\s","");

        int index = modVersion.lastIndexOf('-');
        if (index != -1)
        {
            String lastPart = modVersion.substring(index + 1);
            if (StringUtils.isAlpha(lastPart))
                modVersion = modVersion.substring(0, index);
        }

        return modVersion;
    }

    public static boolean isDevVersion(String version)
    {
        return version.equals("dev-only") || version.equals("alpha-only") || version.equals("beta-only");
    }
}
