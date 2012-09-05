/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.wizard.newgroup.NewGroupWizard;

/**
 * Handler used to move the selected items to a new group.
 */
public final class MoveToNewGroupHandler implements IHandler
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
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();

        // Create the new group under the root node.
        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
        final NewGroupWizard newGroupWizard = new NewGroupWizard(project.getRootNode());
        final WizardDialog wizardDialog = new WizardDialog(shell, newGroupWizard);
        final int returnCode = wizardDialog.open();

        if (returnCode == Window.CANCEL)
        {
            // Canceled.
            return null;
        }

        final Group newGroup = newGroupWizard.getNewGroup();

        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
        final List<HierarchyMoveDTO> moveItems = new ArrayList<HierarchyMoveDTO>(selection.size());

        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        final Iterator<?> iter = selection.iterator();
        while (iter.hasNext())
        {
            final Object current = iter.next();
            if (current instanceof Group)
            {
                final Group group = (Group) current;
                // We need to do this refresh because the parent group may have changed since we just added a new group above.
                final Group parent = groupService.findGroupByID(group.getParentGroup().getId());
                moveItems.add(new HierarchyMoveDTO(group, parent, newGroup));
            }
            else if (current instanceof ResearchFile)
            {
                final ResearchFile researchFile = (ResearchFile) current;
                // We need to do this refresh because the parent group may have changed since we just added a new group above.
                final Group parent = groupService.findGroupByID(researchFile.getParentGroup().getId());
                moveItems.add(new HierarchyMoveDTO(researchFile, parent, newGroup));
            }
            else
            {
                throw new RuntimeException("Trying to move a node that is not a group or research file");
            }
        }

        final String errors = groupService.performHierarchyMove(moveItems);
        if (errors != null)
        {
            MessageDialog.openError(shell, "Could not create group from selected items", errors); 
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
