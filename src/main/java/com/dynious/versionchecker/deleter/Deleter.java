package com.dynious.versionchecker.deleter;

import java.io.*;

public class Deleter
{
    public static void main(String[] args)
    {
        if (args.length >= 1)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            String path = args[0];
            deleteModsFromFile(path);
        }
        System.exit(0);
    }

    public static void deleteModsFromFile(String path)
    {
        try
        {
            File deleteFile = new File(path);
            BufferedReader reader = new BufferedReader(new FileReader(deleteFile));

            String line;
            while((line = reader.readLine()) != null)
            {
                File file = new File(line);
                if (!file.delete())
                {
                    file.deleteOnExit();
                }
            }
            reader.close();
            if (!deleteFile.delete())
            {
                deleteFile.deleteOnExit();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
