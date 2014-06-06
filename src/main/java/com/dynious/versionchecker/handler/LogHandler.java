package com.dynious.versionchecker.handler;

import com.dynious.versionchecker.lib.Reference;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHandler
{
    private static Logger logger = LogManager.getLogger(Reference.MOD_ID);

    public static void log(Level logLevel, Object object)
    {
        logger.log(logLevel, String.valueOf(object));
    }

    public static void error(Object object)
    {
        log(Level.ERROR, object);
    }

    public static void debug(Object object)
    {
        log(Level.DEBUG, object);
    }

    public static void warning(Object object)
    {
        log(Level.WARN, object);
    }

    public static void info(Object object)
    {
        log(Level.INFO, object);
    }
}
