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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFileSortField;
import au.org.intersect.exsite9.domain.SortFieldDirection;
import au.org.intersect.exsite9.service.IGroupService;

/**
 * Command handler to configure a group's sorting behavior.
 */
public final class SortResearchFilesInGroupHandler implements IHandler
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
        final String sortFieldParam = event.getParameter("au.org.intersect.exsite9.commands.SortResearchFilesInGroupCommand.sortField");
        final String sortDirectionParam = event.getParameter("au.org.intersect.exsite9.commands.SortResearchFilesInGroupCommand.sortDirection");

        final ResearchFileSortField sortField = Enum.valueOf(ResearchFileSortField.class, sortFieldParam);
        final SortFieldDirection sortDirection = Enum.valueOf(SortFieldDirection.class, sortDirectionParam);

        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();

        final Object firstElement = selection.getFirstElement();
        final Group selectedGroup;
        if (firstElement instanceof Project)
        {
            selectedGroup = ((Project) firstElement).getRootNode();
        }
        else
        {
            selectedGroup = (Group) selection.getFirstElement();
        }

        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);

        groupService.sortResearchFilesInGroup(selectedGroup, sortField, sortDirection);

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
