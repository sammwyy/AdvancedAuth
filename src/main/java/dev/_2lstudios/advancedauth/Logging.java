package dev._2lstudios.advancedauth;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Logging {
    private static Logger logger;
    private static boolean enabled;

    public static void setLogger(final Logger logger) {
        Logging.logger = logger;
    }

    public static void setEnabled(final boolean enabled) {
        Logging.enabled = enabled;
    }

    public static void log(final Level level, final String message) {
        if (!Logging.enabled) {
            return;
        }
        Logging.logger.log(level, message);
    }

    public static void info(final String message) {
        if (!Logging.enabled) {
            return;
        }
        Logging.logger.info(message);
    }

    public static void warning(final String message) {
        if (!Logging.enabled) {
            return;
        }
        Logging.logger.warning(message);
    }

    public static void severe(final String message) {
        if (!Logging.enabled) {
            return;
        }
        Logging.logger.severe(message);
    }
}
