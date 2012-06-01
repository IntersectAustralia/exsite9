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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.google.common.base.Strings;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IGroupService;

/**
 * The handler to add a group to a node.
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
        
        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final InputDialog dialog = new InputDialog(shell, "New Group", "Add a new group", "", null);
        dialog.open();

        if (dialog.getReturnCode() != Dialog.OK)
        {
            return null;
        }
        final String newGroupName = dialog.getValue();
        if (Strings.isNullOrEmpty(newGroupName))
        {
            return null;
        }

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

        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        final Group newGroup = groupService.createNewGroup(newGroupName);
        groupService.addChildGroup(parentGroup, newGroup);
        
        return null;
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
