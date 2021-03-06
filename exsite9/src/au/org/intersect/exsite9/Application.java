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
