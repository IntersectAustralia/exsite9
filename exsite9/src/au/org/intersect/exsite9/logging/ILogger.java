/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.logging;

public interface ILogger
{
    /**
     * Logs an info message.
     * @param message The message to log.
     */
    void info(final String message);

    /**
     * Logs a warning message.
     * @param message The warning to log.
     */
    void warning(final String message);

    /**
     * Logs a warning message and a throwable.
     * @param message The message to log.
     * @param throwable The throwable to log.
     */
    void warning(final String message, final Throwable throwable);

    /**
     * Logs an error message.
     * @param message The message to log.
     */
    void error(final String message);

    /**
     * Logs an error message and a throwable
     * @param message The message to log.
     * @param throwable The throwable to log.
     */
    void error(final String message, final Throwable throwable);
}
