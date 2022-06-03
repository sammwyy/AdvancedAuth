package dev._2lstudios.advancedauth.security;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import dev._2lstudios.advancedauth.AdvancedAuth;

/**
 * Implements a filter for Log4j to skip sensitive commands.
 *
 * @author Xephi59
 */
public class ConsoleFilter extends AbstractFilter {
    private ConsoleFilterHelper helper;

    public ConsoleFilter(AdvancedAuth plugin) {
        this.helper = new ConsoleFilterHelper(plugin);
    }

    /**
     * Validates a Message instance and returns the {@link Result} value
     * depending on whether the message contains sensitive data.
     *
     * @param message The Message object to verify
     *
     * @return The Result value
     */
    private Result validateMessage(Message message) {
        if (message == null) {
            return Result.NEUTRAL;
        }
        return validateMessage(message.getFormattedMessage());
    }

    /**
     * Validates a message and returns the {@link Result} value depending
     * on whether the message contains sensitive data.
     *
     * @param message The message to verify
     *
     * @return The Result value
     */
    private Result validateMessage(String message) {
        return this.helper.hasSilentCommand(message)
            ? Result.DENY
            : Result.NEUTRAL;
    }

    @Override
    public Result filter(LogEvent event) {
        Message candidate = null;
        if (event != null) {
            candidate = event.getMessage();
        }
        return validateMessage(candidate);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        String candidate = null;
        if (msg != null) {
            candidate = msg.toString();
        }
        return validateMessage(candidate);
    }
}