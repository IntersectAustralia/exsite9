/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view;

import java.io.File;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.ViewPart;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IFileService;
import au.org.intersect.exsite9.service.IProjectService;
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
        // This is used to the view can load the project with the New Project command is executed.
        final Command newProjectCommand = commandService.getCommand("au.org.intersect.exsite9.commands.NewProjectCommand");
        newProjectCommand.addExecutionListener(this);

        initContextMenu();
    }

    /**
     * Initializes the context menu for items in the tree view.
     */
    private void initContextMenu()
    {
        final MenuManager menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new IMenuListener()
        {
            @Override
            public void menuAboutToShow(final IMenuManager manager)
            {
                if (ProjectExplorerView.this.treeViewer.getSelection().isEmpty())
                {
                    return;
                }

                final IStructuredSelection selection = (IStructuredSelection) ProjectExplorerView.this.treeViewer.getSelection();
                if (selection.size() > 1)
                {
                    return;
                }

                final Object selectedElement = selection.getFirstElement();

                if (selectedElement instanceof Project)
                {
                    final Project project = (Project) selectedElement;
                    // TODO: how do we really do this in a way that is reusable?
                    manager.add(new Action()
                    {
                        @Override
                        public String getText()
                        {
                            return "Add Folder";
                        }

                        @Override
                        public void run()
                        {
                            final DirectoryDialog directoryDialog = new DirectoryDialog(getSite().getShell(), SWT.OPEN);
                            directoryDialog.setMessage("Choose a folder to add to the project.");
                            directoryDialog.setText("Choose a folder");

                            final String path = directoryDialog.open();
                            if (path != null)
                            {
                                final File directory = new File(path);

                                if (!directory.exists() || !directory.isDirectory() || !directory.canRead())
                                {
                                    MessageDialog.openError(getSite().getShell(), "Error", "Provided folder does not exist or is not readable.");
                                    return;
                                }

                                final Folder folder = new Folder(directory);

                                final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
                                final IFileService fileService = (IFileService) PlatformUI.getWorkbench().getService(IFileService.class);

                                projectService.mapFolderToProject(project, folder);
                                fileService.identifyNewFilesForProject(project);

                                ProjectExplorerView.this.treeViewer.refresh();
                                ProjectExplorerView.this.treeViewer.expandAll();
                            }
                        }
                    });
                }
            }
        });
        final Menu menu = menuManager.createContextMenu(this.treeViewer.getTree());
        this.treeViewer.getTree().setMenu(menu);
        getSite().registerContextMenu(menuManager, treeViewer);
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
            if (project != null)
            {
                final ProjectExplorerViewInput wrapper = new ProjectExplorerViewInput(project);
                this.treeViewer.setInput(wrapper);
                this.treeViewer.expandAll();
            }
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
