package com.dynious.versionchecker.helper;

public class MatchHelper
{
    public static boolean doStringsMatch(String first, String second)
    {
        if (first.startsWith("Minecraft "))
            first = first.substring("Minecraft ".length());

        if (second.startsWith("Minecraft "))
            second = second.substring("Minecraft ".length());

        if (first.equals(second))
            return true;

        String[] firstTokens = first.split("\\.");
        String[] secondTokens = second.split("\\.");

        String[] mostTokens = firstTokens.length > secondTokens.length ? firstTokens : secondTokens;
        String[] leastTokens = mostTokens == firstTokens ? secondTokens : firstTokens;

        if (firstTokens.length != secondTokens.length && !isVersionWildcardPattern(leastTokens[leastTokens.length - 1]))
            return false;

        for (int i = 0; i < leastTokens.length; i++)
        {
            if (!leastTokens[i].equals(mostTokens[i]) && !(isVersionWildcardPattern(leastTokens[i]) || isVersionWildcardPattern(mostTokens[i])))
                return false;
        }

        return true;
    }

    public static boolean isVersionWildcardPattern(String str)
    {
        return str.equals("*") || str.equals("x");
    }
}
