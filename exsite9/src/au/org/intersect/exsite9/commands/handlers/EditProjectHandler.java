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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.wizard.editproject.EditProjectWizard;

/**
 *
 * @version $Rev: 29 $
 */
public class EditProjectHandler implements IHandler
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
        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
        final Object selectedObject = selection.getFirstElement();
        Project selectedProject = (Project) selectedObject;
        
        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final EditProjectWizard wizard = new EditProjectWizard(selectedProject);
        final WizardDialog wizardDialog = new WizardDialog(shell, wizard);
        wizardDialog.open();

        selectedProject = wizard.updateProject();
        return selectedProject;
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