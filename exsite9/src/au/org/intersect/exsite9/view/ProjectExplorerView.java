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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewContentProvider;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewInput;
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

        this.treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        this.treeViewer.setContentProvider(new ProjectExplorerViewContentProvider());
        this.treeViewer.setLabelProvider(new ProjectExplorerViewLabelProvider());

        final ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(
                ICommandService.class);

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

        final Command reloadProjectCommand = commandService
                .getCommand("au.org.intersect.exsite9.commands.ReloadProjectCommand");
        reloadProjectCommand.addExecutionListener(this);
        
        final Command openProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.OpenProjectCommand");
        openProjectCommand.addExecutionListener(this);

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
        getSite().setSelectionProvider(this.treeViewer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus()
    {

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
        if (commandId.equals("au.org.intersect.exsite9.commands.NewProjectCommand"))
        {
            displayProjectAndExpand(returnValue);
        }
        else if (commandId.equals("au.org.intersect.exsite9.commands.OpenProjectCommand"))
        {
            displayProjectAndExpand(returnValue);
        }
        else if (commandId.equals("au.org.intersect.exsite9.commands.AddFolderToProjectCommand"))
        {
            // The Project object is already bound - no need to get another.
            refreshAndExpand();
            
        }
        else if (commandId.equals("au.org.intersect.exsite9.commands.ReloadProjectCommand"))
        {
            refreshAndExpand();
        }
        else if (commandId.equals("au.org.intersect.exsite9.commands.AddGroup"))
        {
            refreshAndExpand();
        }
    }

    private void refreshAndExpand()
    {
        this.treeViewer.refresh();
        this.treeViewer.expandAll();
    }
    
    private void displayProjectAndExpand(final Object returnValue)
    {
        final Project project = (Project) returnValue;
        if (project != null)
        {
            final ProjectExplorerViewInput wrapper = new ProjectExplorerViewInput(project);
            this.treeViewer.setInput(wrapper);
            this.treeViewer.expandAll();
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
