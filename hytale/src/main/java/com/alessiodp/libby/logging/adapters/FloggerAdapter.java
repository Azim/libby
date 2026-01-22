package com.alessiodp.libby.logging.adapters;

import com.alessiodp.libby.logging.LogLevel;
import com.google.common.flogger.AbstractLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import static java.util.Objects.requireNonNull;

/**
 * Logging adapter that logs to a flogger.
 */
public class FloggerAdapter implements LogAdapter {
    /**
     * JDK logger
     */
    private final AbstractLogger logger;

    /**
     * Creates a new flogger adapter that logs to a {@link AbstractLogger}.
     *
     * @param logger {@link AbstractLogger} to wrap
     */
    public FloggerAdapter(@NotNull AbstractLogger logger) {
        this.logger = requireNonNull(logger, "logger");
    }

    /**
     * Logs a message with the provided level to the {@link AbstractLogger}.
     *
     * @param level   message severity level
     * @param message the message to log
     */
    @Override
    public void log(@NotNull LogLevel level, @Nullable String message) {
        switch (requireNonNull(level, "level")) {
            case DEBUG:
                logger.atFine().log(message);
                break;
            case INFO:
                logger.atInfo().log(message);
                break;
            case WARN:
                logger.atWarning().log(message);
                break;
            case ERROR:
                logger.atSevere().log(message);
                break;
        }
    }

    /**
     * Logs a message and stack trace with the provided level to the {@link AbstractLogger}.
     *
     * @param level     message severity level
     * @param message   the message to log
     * @param throwable the throwable to print
     */
    @Override
    public void log(@NotNull LogLevel level, @Nullable String message, @Nullable Throwable throwable) {
        switch (requireNonNull(level, "level")) {
            case DEBUG:
                logger.atFine().log(message, throwable);
                break;
            case INFO:
                logger.atInfo().log(message, throwable);
                break;
            case WARN:
                logger.atWarning().log(message, throwable);
                break;
            case ERROR:
                logger.atSevere().log(message, throwable);
                break;
        }
    }
}
