/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.jobs.IdentifyAllNewFilesForProjectRepeatingJob;


/**
 * This is the main entry point of the Eclipse RCP Application.
 */
public final class Application implements IApplication
{

    /**
     * {@inheritDoc}
     */
    public Object start(final IApplicationContext context) throws Exception
    {
        final Display display = PlatformUI.createDisplay();
        try
        {
            // Start the scheduled find files job
            try
            {
                Integer refreshTimeInMins = Integer.parseInt(System.getProperty("exsite9.refreshTimeInMins"));
                if(refreshTimeInMins != null)
                {
                    if (refreshTimeInMins>0)
                    {
                        long delay = refreshTimeInMins * 60 * 1000;
                        IdentifyAllNewFilesForProjectRepeatingJob findFilesJob = new IdentifyAllNewFilesForProjectRepeatingJob(delay);
                        findFilesJob.schedule(delay);
                    }
                }
            }
            catch(NumberFormatException nfe)
            {
                // TODO: Figure out what we want to do here.
            }
            
            final int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART)
            {
                return IApplication.EXIT_RESTART;
            }
            else
            {
                return IApplication.EXIT_OK;
            }
        }
        finally
        {
            display.dispose();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stop()
    {
        if (!PlatformUI.isWorkbenchRunning())
        {
            return;
        }

        final IWorkbench workbench = PlatformUI.getWorkbench();
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable()
        {
            public void run()
            {
                if (!display.isDisposed())
                    workbench.close();
            }
        });
    }
}
