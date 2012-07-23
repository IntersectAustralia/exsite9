/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.commands.handlers;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.wizard.listfolders.ListFoldersWizard;

public class ListFoldersHandler implements IHandler
{
    private static final Logger LOG = Logger.getLogger(ListFoldersHandler.class);
    
    @Override
    public void addHandlerListener(IHandlerListener handlerListener)
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
        final ListFoldersWizard wizard = new ListFoldersWizard();
        final WizardDialog wizardDialog = new WizardDialog(shell, wizard);
        wizardDialog.open();
        

        final IHandlerService handlerService = (IHandlerService) activeWorkbenchWindow.getService(IHandlerService.class);
        
        try
        {
            handlerService.executeCommand("au.org.intersect.exsite9.commands.ReloadProjectCommand", null);
        }
        catch (NotDefinedException e)
        {
            LOG.error("Cannot execute reload project command", e);
        }
        catch (NotEnabledException e)
        {
            LOG.error("Cannot execute reload project command", e);
        }
        catch (NotHandledException e)
        {
            LOG.error("Cannot execute reload project command", e);
        }
        
        return null;
    }

    @Override
    public boolean isEnabled()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project currentproject = projectManager.getCurrentProject();
        
        return (currentproject == null) ? false : true;
    }

    @Override
    public boolean isHandled()
    {
        return true;
    }

    @Override
    public void removeHandlerListener(IHandlerListener handlerListener)
    {
        // TODO Auto-generated method stub
        
    }

}
