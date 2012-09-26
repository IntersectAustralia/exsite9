/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
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

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.wizard.newgroup.NewGroupWizard;

/**
 * Command handler to add a group to a project, via the plugin.xml
 */
public final class AddGroupHandler implements IHandler
{

    /**
     * @{inheritDoc}
     */
    @Override
    public void addHandlerListener(final IHandlerListener handlerListener)
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void dispose()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException
    {
        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
        final Object selectedObject = selection.getFirstElement();
        
        final Group parentGroup;
        if (selectedObject instanceof Group)
        {
            parentGroup = (Group) selectedObject;
        }
        else if (selectedObject instanceof Project)
        {
            final Project project = (Project) selectedObject;
            parentGroup = project.getRootNode();
        }
        else
        {
            // Should never happen
            throw new RuntimeException("Trying to add a group to an element that is not a project or group.");
        }

        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final NewGroupWizard newGroupWizard = new NewGroupWizard(parentGroup);
        final WizardDialog wizardDialog = new WizardDialog(shell, newGroupWizard);
        wizardDialog.open();

        return newGroupWizard.getNewGroup();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isHandled()
    {
        return true;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void removeHandlerListener(final IHandlerListener handlerListener)
    {
    }
}
