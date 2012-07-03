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
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.view.provider.ProjectViewInputWrapper;
import au.org.intersect.exsite9.view.provider.SubmissionPackageBrowserViewContentProvider;
import au.org.intersect.exsite9.view.provider.SubmissionPackageBrowserViewLabelProvider;

/**
 * The view that shows the submission packages
 */
public final class SubmissionPackageBrowserView extends ViewPart implements IExecutionListener
{
    public static final String ID = SubmissionPackageBrowserView.class.getName();

    private TreeViewer treeViewer;

    /**
     * Constructor
     */
    public SubmissionPackageBrowserView()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createPartControl(final Composite parent)
    {
        final ICommandService commandService = (ICommandService) PlatformUI.getWorkbench().getService(ICommandService.class);
        
        final Command createSubmissionPackageCommand = commandService.getCommand("au.org.intersect.exsite9.commands.CreateSubmissionPackageCommand");
        createSubmissionPackageCommand.addExecutionListener(this);
        
        final Command openProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.OpenProjectCommand");
        openProjectCommand.addExecutionListener(this);
        
        final Command newProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.NewProjectCommand");
        newProjectCommand.addExecutionListener(this);

        final Command editSubmissionPackageCommand = commandService.getCommand("au.org.intersect.exsite9.commands.EditSubmissionPackageCommand");
        editSubmissionPackageCommand.addExecutionListener(this);

        this.setPartName("Submission Package Browser");

        this.treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        this.treeViewer.setContentProvider(new SubmissionPackageBrowserViewContentProvider());
        this.treeViewer.setLabelProvider(new SubmissionPackageBrowserViewLabelProvider());

        loadCurrentProject();

        // Initialize the context menu
        final MenuManager menuManager = new MenuManager();
        final Menu menu = menuManager.createContextMenu(this.treeViewer.getTree());
        this.treeViewer.getTree().setMenu(menu);
        getSite().registerContextMenu(menuManager, this.treeViewer);

        // This allows other views to listen to selection changes.
        getSite().setSelectionProvider(this.treeViewer);
    }

    private void loadCurrentProject()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        if (project != null)
        {
            final ProjectViewInputWrapper wrapper = new ProjectViewInputWrapper(project);
            this.treeViewer.setInput(wrapper);
            this.treeViewer.expandAll();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setFocus()
    {
    }

    @Override
    public void notHandled(final String commandId, final NotHandledException exception)
    {
    }

    @Override
    public void postExecuteFailure(final String commandId, final ExecutionException exception)
    {
    }

    @Override
    public void postExecuteSuccess(final String commandId, final Object returnValue)
    {
        loadCurrentProject();
    }

    @Override
    public void preExecute(final String commandId, final ExecutionEvent event)
    {
    }
}