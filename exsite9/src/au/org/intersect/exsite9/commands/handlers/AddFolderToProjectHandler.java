/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IFileService;
import au.org.intersect.exsite9.service.IProjectService;

/**
 * 
 */
public final class AddFolderToProjectHandler implements IHandler
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
        final Project project = (Project) selection.getFirstElement();

        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

        final DirectoryDialog directoryDialog = new DirectoryDialog(shell, SWT.OPEN);
        directoryDialog.setMessage("Choose a folder to add to the project.");
        directoryDialog.setText("Choose a folder");

        final String path = directoryDialog.open();
        if (path != null)
        {
            final File directory = new File(path);

            if (!directory.exists() || !directory.isDirectory() || !directory.canRead())
            {
                MessageDialog.openError(shell, "Error", "Provided folder does not exist or is not readable.");
                return null;
            }

            final Folder folder = new Folder(directory);

            final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
            final IFileService fileService = (IFileService) PlatformUI.getWorkbench().getService(IFileService.class);

            projectService.mapFolderToProject(project, folder);
            fileService.identifyNewFilesForProject(project);
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
    public void removeHandlerListener(IHandlerListener handlerListener)
    {

    }
}