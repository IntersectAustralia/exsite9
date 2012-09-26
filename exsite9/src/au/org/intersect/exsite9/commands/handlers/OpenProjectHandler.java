/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: intersect_codetemplates.xml 29 2010-07-16 05:45:06Z georgina $
 */
package au.org.intersect.exsite9.commands.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.jobs.IdentifyAllNewFilesForProjectJob;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.wizard.openproject.OpenProjectWizard;

/**
 * Command handler to open an existing project, via the plugin.xml
 */
public class OpenProjectHandler implements IHandler
{

    @Override
    public void addHandlerListener(final IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
        final Shell shell = activeWorkbenchWindow.getShell();
        final OpenProjectWizard wizard = new OpenProjectWizard();
        final WizardDialog wizardDialog = new WizardDialog(shell, wizard);
        wizardDialog.open();

        final Project project = wizard.getSelectedProject();

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);

        if (project != null)
        {
            projectManager.setCurrentProjectID(project.getId());
            Job identifyAllNewFilesForProject = new IdentifyAllNewFilesForProjectJob();
            identifyAllNewFilesForProject.schedule();
            HandlerUtils.activateShowProjectActivity(activeWorkbenchWindow);
            
            shell.setText("ExSite9 - " + project.getName());
        }

        return project;
    }

    @Override
    public boolean isEnabled()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isHandled()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void removeHandlerListener(final IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub
        
    }

}
