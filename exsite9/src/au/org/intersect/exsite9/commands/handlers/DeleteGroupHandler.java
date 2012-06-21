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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IProjectService;

/**
 * Executed to delete a group.
 */
public final class DeleteGroupHandler implements IHandler
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

        if (!(selectedObject instanceof Group) || (selectedObject instanceof NewFilesGroup))
        {
            throw new RuntimeException("Trying to delete a non-group node from the DeleteGroupHandler");
        }

        final Group groupToDelete = (Group) selectedObject;

        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final boolean confirm = MessageDialog.openConfirm(shell, "Are you sure?", "Are you sure you want to delete group '" + groupToDelete.getName() +
                "'? All child groups and files will be moved to the parent.");

        if (confirm)
        {
            final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
            groupService.deleteGroup(groupToDelete);

            // We need to do this because we do not directly update Project's object model, so we need to get a fresh object from the db.
            final IProjectManager projectManagerService = (IProjectManager) PlatformUI.getWorkbench().getService(
                    IProjectManager.class);
            final Project currentProject = projectManagerService.getCurrentProject();

            // Since we did not update the project object model - we need to referesh it.
            final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
            final Project updatedProject = projectService.findProjectById(currentProject.getId());
            projectManagerService.setCurrentProject(updatedProject);
        }

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
