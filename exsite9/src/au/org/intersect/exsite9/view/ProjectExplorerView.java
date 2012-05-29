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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
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

        this.treeViewer = new TreeViewer(parent);
        this.treeViewer.setContentProvider(new ProjectExplorerViewContentProvider());
        this.treeViewer.setLabelProvider(new ProjectExplorerViewLabelProvider());

        final ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);

        // This command is defined in the plugin.xml
        final Command newProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.NewProjectCommand");
        newProjectCommand.addExecutionListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus()
    {

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void notHandled(final String commandId, final NotHandledException exception)
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void postExecuteFailure(final String commandId, final ExecutionException exception)
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void postExecuteSuccess(final String commandId, final Object returnValue)
    {
        if (commandId.equals("au.org.intersect.exsite9.commands.NewProjectCommand"))
        {
            final Project project = (Project) returnValue;
            final ProjectExplorerViewInput wrapper = new ProjectExplorerViewInput(project);
            this.treeViewer.setInput(wrapper);
            this.treeViewer.expandAll();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void preExecute(final String commandId, final ExecutionEvent event)
    {
    }
}
