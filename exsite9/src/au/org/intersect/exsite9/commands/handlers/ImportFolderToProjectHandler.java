/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.commands.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.jobs.ConsolidateFoldersJob;
import au.org.intersect.exsite9.jobs.ImportFolderStructureIntoProjectJob;
import au.org.intersect.exsite9.service.IProjectManager;

/**
 * 
 */
public final class ImportFolderToProjectHandler implements IHandler
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

        if (project == null)
        {
            throw new IllegalStateException("Trying to add a folder to a null project");
        }

        final Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

        final DirectoryDialog directoryDialog = new DirectoryDialog(shell, SWT.OPEN);
        directoryDialog.setMessage("Choose a folder to import into the project.");
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

            // Check if we are already watching the folder, or if the folder is a descendant of a folder we are already watching.
            final List<Folder> subFoldersOfNewFolder = new ArrayList<Folder>();
            final List<Folder> watchedFolders = project.getFolders();
            for(final Folder watched : watchedFolders)
            {
                if(folder.getFolder().getAbsolutePath().equals(watched.getFolder().getAbsolutePath()))
                {
                    MessageDialog.openError(shell, "Error", "The folder is already being watched.");
                    return null;
                }
                else if(folder.getFolder().getAbsolutePath().startsWith(watched.getFolder().getAbsolutePath() + File.separatorChar))
                {
                    MessageDialog.openError(shell, "Error", "The folder is already being watched as it is a sub-folder of a watched folder.");
                    return null;
                }
                else if(watched.getFolder().getAbsolutePath().startsWith(folder.getFolder().getAbsolutePath() + File.separatorChar))
                {
                    subFoldersOfNewFolder.add(watched);
                }
            }

            Job importFolderStructureIntoProject = new ImportFolderStructureIntoProjectJob(folder);
            importFolderStructureIntoProject.schedule();
            
            if( ! subFoldersOfNewFolder.isEmpty())
            {
                Job consolidateFolders = new ConsolidateFoldersJob(folder, subFoldersOfNewFolder);
                consolidateFolders.schedule();
            }
        }
        return null;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isEnabled()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project currentProject = projectManager.getCurrentProject();
       
        return (currentProject == null) ? false : true;
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