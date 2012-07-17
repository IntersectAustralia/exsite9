/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectManager;

/**
 * Handles excluding of Research Files.
 */
public final class ExcludeResearchFilesHandler implements IHandler
{
    private static final Logger LOG = Logger.getLogger(ExcludeResearchFilesHandler.class);

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

        // This can be executed on one more more ResearchFiles
        final List<ResearchFile> toExclude = new ArrayList<ResearchFile>(selection.size());

        for (final Object selectedObj : selection.toList())
        {
            if (selectedObj instanceof ResearchFile)
            {
                toExclude.add((ResearchFile) selectedObj);
            }
            else
            {
                LOG.warn("Trying to exclude a node that is not a ResearchFile. Ignoring.");
            }
        }

        // Setup the hierarchical move DTO Objects.
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);

        final Project project = projectManager.getCurrentProject();
        final Group destGroup = project.getExcludedFilesNode();

        final List<HierarchyMoveDTO> moveObjects = new ArrayList<HierarchyMoveDTO>(toExclude.size());

        for (final ResearchFile rf : toExclude)
        {
            moveObjects.add(new HierarchyMoveDTO(rf, rf.getParentGroup(), destGroup));
        }

        final String out = groupService.performHierarchyMove(moveObjects);
        if (out != null)
        {
            final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
            MessageDialog.openError(shell, "Could not exclude Research Files", out); 
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
