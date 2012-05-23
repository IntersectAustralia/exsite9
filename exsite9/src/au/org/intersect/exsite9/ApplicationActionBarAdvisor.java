/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 *
 */
public final class ApplicationActionBarAdvisor extends ActionBarAdvisor
{

    public ApplicationActionBarAdvisor(final IActionBarConfigurer configurer)
    {
        super(configurer);
    }

    protected void makeActions(final IWorkbenchWindow window)
    {
    }

    protected void fillMenuBar(final IMenuManager menuBar)
    {
    }

}
