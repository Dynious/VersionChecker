package com.dynious.versionchecker.helper;

public class MatchHelper
{

    public static boolean doStringsMatch(String fullString, String stringToMatch)
    {
        if (fullString.isEmpty())
        {
            return stringToMatch.isEmpty() || stringToMatch.equals("*");
        }

        int place = 0;
        char[] fullStringArr = fullString.toCharArray();
        char[] matchStringArr = stringToMatch.toCharArray();
        for (int i = 0; i < matchStringArr.length; i++)
        {
            char c = matchStringArr[i];
            if (c == '*')
            {
                if (i + 1 >= matchStringArr.length)
                    return true;

                char nextChar = matchStringArr[i + 1];
                boolean found = false;
                for (int j = place; j < fullStringArr.length; j++)
                {
                    if (fullStringArr[j] == nextChar)
                    {
                        place = j;
                        i++;
                        found = true;
                        break;
                    }
                }
                if (!found)
                    return false;
            }
            else
            {
                if (c != fullStringArr[place])
                    return false;
            }
            place++;
        }
        return place == (fullStringArr.length - 1);
    }
}
