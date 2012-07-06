/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.view.listener.ProjectExplorerDragListener;
import au.org.intersect.exsite9.view.listener.ProjectExplorerDropListener;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewContentProvider;
import au.org.intersect.exsite9.view.provider.ProjectViewInputWrapper;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewLabelProvider;

/**
 * This is the ViewPart that will hold the Project Explorer UI component.
 */
public final class ProjectExplorerView extends ViewPart implements IExecutionListener
{
    // This needs to match what is defined in the plugin.xml
    public static final String ID = ProjectExplorerView.class.getName();

    private TreeViewer treeViewer;

    /**
     * Constructor
     */
    public ProjectExplorerView()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPartControl(final Composite parent)
    {
        this.setPartName("Project View");

        this.treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        this.treeViewer.setContentProvider(new ProjectExplorerViewContentProvider(true));
        this.treeViewer.setLabelProvider(new ProjectExplorerViewLabelProvider());
        ColumnViewerToolTipSupport.enableFor(this.treeViewer);

        final ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(
                ICommandService.class);

        // Set up drag & drop
        int operations = DND.DROP_MOVE;
        Transfer[] transferTypes = new Transfer[] {LocalSelectionTransfer.getTransfer()};
        ProjectExplorerDragListener dragListener = new ProjectExplorerDragListener(treeViewer);
        ProjectExplorerDropListener dropListener = new ProjectExplorerDropListener(treeViewer);
        this.treeViewer.addDragSupport(operations, transferTypes, dragListener);
        this.treeViewer.addDropSupport(operations, transferTypes, dropListener);

        // This command is defined in the plugin.xml
        // This is used to the view can load the project with the New Project command is executed.
        final Command newProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.NewProjectCommand");
        newProjectCommand.addExecutionListener(this);

        final Command addFolderToProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.AddFolderToProjectCommand");
        addFolderToProjectCommand.addExecutionListener(this);

        final Command addGroupCommand = commandService.getCommand("au.org.intersect.exsite9.commands.AddGroup");
        addGroupCommand.addExecutionListener(this);

        final Command deleteGroupCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.DeleteGroupCommand");
        deleteGroupCommand.addExecutionListener(this);

        final Command reloadProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.ReloadProjectCommand");
        reloadProjectCommand.addExecutionListener(this);

        final Command openProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.OpenProjectCommand");
        openProjectCommand.addExecutionListener(this);

        final Command editProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.EditProjectCommand");
        editProjectCommand.addExecutionListener(this);

        final Command renameGroupCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.RenameGroupCommand");
        renameGroupCommand.addExecutionListener(this);

        final Command listFoldersCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.ListFoldersCommand");
        listFoldersCommand.addExecutionListener(this);

        initContextMenu();
    }

    /**
     * Initializes the context menu for items in the tree view.
     */
    private void initContextMenu()
    {
        final MenuManager menuManager = new MenuManager();
        final Menu menu = menuManager.createContextMenu(this.treeViewer.getTree());
        this.treeViewer.getTree().setMenu(menu);
        getSite().registerContextMenu(menuManager, this.treeViewer);

        // This allows other views to listen to selection changes.
        getSite().setSelectionProvider(this.treeViewer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus()
    {
        final MetadataBrowserView metadataBrowserView  = (MetadataBrowserView) ViewUtils.getViewByID(
                PlatformUI.getWorkbench().getActiveWorkbenchWindow(), MetadataBrowserView.ID);
        metadataBrowserView.setEnabled(true);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void notHandled(final String commandId, final NotHandledException exception)
    {
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void postExecuteFailure(final String commandId, final ExecutionException exception)
    {
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void postExecuteSuccess(final String commandId, final Object returnValue)
    {
        if (commandId.equals("au.org.intersect.exsite9.commands.NewProjectCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.OpenProjectCommand"))
        {
            if (returnValue != null)
            {
                displayProjectAndExpand();
            }
        }

        else if (commandId.equals("au.org.intersect.exsite9.commands.EditProjectCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.AddFolderToProjectCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.AddGroup")
                || commandId.equals("au.org.intersect.exsite9.commands.RenameGroupCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.DeleteGroupCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.ReloadProjectCommand")
                || commandId.equals("au.org.intersect.exsite9.commands.ListFoldersCommand"))
        {
            refresh();
        }
    }

    public void refresh()
    {
        this.treeViewer.refresh();
    }

    public ISelection getSelection()
    {
        return this.treeViewer.getSelection();
    }

    private void displayProjectAndExpand()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(
                IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        if (project != null)
        {
            final ProjectViewInputWrapper wrapper = new ProjectViewInputWrapper(project);
            this.treeViewer.setInput(wrapper);
            this.treeViewer.expandToLevel(2);
        }
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void preExecute(final String commandId, final ExecutionEvent event)
    {
    }
}
