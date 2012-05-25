/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.logging;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;

import au.org.intersect.exsite9.Application;

/**
 * Provides facilities to log messages via Eclipse's standard logging framework.
 */
public final class EclipseRCPLogger implements ILogger
{
    private static final String PLUGIN_ID = Application.class.getPackage().getName();

    private final StatusManager statusManager;

    /**
     * Constructor
     */
    public EclipseRCPLogger()
    {
        this(StatusManager.getManager());
    }

    /**
     * Constructor for use in unit tests only.
     * @param statusManager The status manager.
     */
    EclipseRCPLogger(final StatusManager statusManager)
    {
        this.statusManager = statusManager;
    }

    /**
     * @{inheritDoc}
     */
    public void info(final String message)
    {
        final Status status = new Status(IStatus.INFO, PLUGIN_ID, IStatus.INFO, message, null);
        statusManager.handle(status);
    }

    /**
     * @{inheritDoc}
     */
    public void warning(final String message)
    {
        warning(message, null);
    }

    /**
     * @{inheritDoc}
     */
    public void warning(final String message, final Throwable throwable)
    {
        final Status status = new Status(IStatus.WARNING, PLUGIN_ID, IStatus.WARNING, message, throwable);
        statusManager.handle(status);
    }

    /**
     * @{inheritDoc}
     */
    public void error(final String message)
    {
        error(message, null);
    }

    /**
     * @{inheritDoc}
     */
    public void error(final String message, final Throwable throwable)
    {
        final Status status = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, message, throwable);
        statusManager.handle(status);
    }
}
