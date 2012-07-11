/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * A Runnable that has the ability to determine if an Exception was raised during execution, and provide progress information to the UI.
 */
public abstract class ProgressRunnableWithError implements Runnable, IRunnableWithProgress
{
    private final String message;
    private Exception exception;

    /**
     * Constructor
     * @param message The message to display while the task is being run.
     */
    public ProgressRunnableWithError(final String message)
    {
        this.message = message;
    }

    /**
     * The work to be done.
     * @throws Exception Any exception that is raised and should be handled by the UI.
     */
    public abstract void doRun() throws Exception;

    /**
     * @{inheritDoc}
     */
    @Override
    public void run()
    {
        this.exception = null;
        try
        {
            doRun();
        }
        catch (final Exception e)
        {
            this.exception = e;
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
    {
        monitor.beginTask(message, -1);
        run();
        monitor.done();
    }

    /**
     * Obtains the Exception that was raised during execution of the task.
     * @return The Exception.
     */
    public Exception getException()
    {
        return this.exception;
    }
}
