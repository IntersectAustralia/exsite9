/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * 
 */
public final class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor
{

    public ApplicationWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer)
    {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer)
    {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen()
    {
        final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

        // TODO: we probably want to center it on the screen and have a larger initial size.
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        configurer.setShowProgressIndicator(true);
        configurer.setTitle("ExSite9"); //$NON-NLS-1$
    }
}
