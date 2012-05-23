/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 *
 */
public final class ApplicationActionBarAdvisor extends ActionBarAdvisor
{
    private IWorkbenchAction exitAction;

    /**
     * Constructor
     * @param configurer
     */
    public ApplicationActionBarAdvisor(final IActionBarConfigurer configurer)
    {
        super(configurer);
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void makeActions(final IWorkbenchWindow window)
    {
        this.exitAction = ActionFactory.QUIT.create(window);
        this.register(this.exitAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillMenuBar(final IMenuManager menuBar)
    {
        final MenuManager fileMenu = new MenuManager("File", "file");
        fileMenu.add(this.exitAction);
        menuBar.add(fileMenu);
    }

}
