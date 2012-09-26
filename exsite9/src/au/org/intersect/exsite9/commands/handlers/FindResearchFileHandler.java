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

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.jobs.ConsolidateFoldersJob;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.service.IResearchFileService;

/**
 * Command handler to allow a user to locate a file on disk that is flagged as missing in the project, via the plugin.xml
 */
public class FindResearchFileHandler implements IHandler
{
    private static final Logger LOG = Logger.getLogger(FindResearchFileHandler.class);

    @Override
    public void addHandlerListener(final IHandlerListener handlerListener)
    {
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
        
        try
        {
        final Shell shell = activeWorkbenchWindow.getShell();
        final IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getActiveWorkbenchWindow(event)
                .getActivePage().getSelection();
        final Object selectionObject = selection.getFirstElement();

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(
                IProjectManager.class);
        final Project currentProject = projectManager.getCurrentProject();

        final IResearchFileService researchFileService = (IResearchFileService) PlatformUI.getWorkbench().getService(
                IResearchFileService.class);
        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(
                IProjectService.class);

        FileDialog fileDialog = new FileDialog(shell);
        fileDialog.setText("choose the file in it's new location");

        final String newFilePath = fileDialog.open();

        final File newFileObject = new File(newFilePath);

        if (!newFileObject.exists() || !newFileObject.canRead() || !newFileObject.isFile())
        {
            MessageDialog.openError(shell, "Error", "The file you provided does not exist or is not readable.");
            return null;
        }

        String parentPathOfNewFile = newFileObject.getParent();
        Folder folder = new Folder(new File(parentPathOfNewFile));

        // Watched folder checking
        for (Folder existingFolder : currentProject.getFolders())
        {
            if (existingFolder.getFolder().getAbsolutePath().equalsIgnoreCase(parentPathOfNewFile) || parentPathOfNewFile.startsWith(existingFolder.getFolder().getAbsolutePath()))
            { // means the new location is in a folder that is already being watched or in a sub-folder of a watched folder

                for (ResearchFile existingResearchFile : existingFolder.getFiles())
                {
                    if (existingResearchFile.getFile().equals(newFileObject))
                    {
                        boolean confirmation = MessageDialog.openConfirm(shell, "", "The file you have chosen is already in the system in another location, by proceeding, that file and any associated metadata will be removed.");
                        if (confirmation)
                        {
                            projectService.removeResearchFileFromSystem(existingResearchFile.getId());                            
                            ((ResearchFile) selectionObject).setFile(newFileObject);
                            researchFileService.updateResearchFile((ResearchFile) selectionObject);
                            researchFileService.changeAFilesParentFolder((ResearchFile) selectionObject, existingFolder.getId());
                        }
                        return null;
                    }
                }
                // if it hits this point it means that the folder is already watched or is a sub of a watched folder but the file is not currently in
                // the system
                ((ResearchFile) selectionObject).setFile(newFileObject);
                researchFileService.updateResearchFile((ResearchFile) selectionObject);
                return null;
            }
            else if(existingFolder.getFolder().getAbsolutePath().startsWith(parentPathOfNewFile))
            {//means the location of the relocated file is now in the parent of a watched sub-folder
                
                //we know the file doesn't exist in the system because the file is in the parent of a watched 
                //folder so is basically the same as being in an un-watched folder
                ((ResearchFile) selectionObject).setFile(newFileObject);
                researchFileService.updateResearchFile((ResearchFile) selectionObject);
                
                //add the parent folder to the list of watched folders and consolidate the files from the sub-folder
                projectService.mapFolderToProject(currentProject, folder);
                
                final List<Folder> subFoldersOfNewFolder = new ArrayList<Folder>();
                subFoldersOfNewFolder.add(existingFolder);
                Job consolidateFolders = new ConsolidateFoldersJob(folder, subFoldersOfNewFolder);
                consolidateFolders.schedule();  
            }
            
        }

        // non-watched folder
        boolean startWatchingFolder = MessageDialog.openConfirm(shell, "Watch Folder?", "The file you selected is not currently within a watched folder, to change the location of this file to your selected directory the system must begin watching that directory, do you want to do this?");

        if (startWatchingFolder)
        {
            ((ResearchFile) selectionObject).setFile(newFileObject);
            researchFileService.updateResearchFile((ResearchFile) selectionObject);
            projectService.mapFolderToProject(currentProject, folder);
            return null;
        }
        return null;
        }
        finally
        {
            final IHandlerService handlerService = (IHandlerService) activeWorkbenchWindow.getService(IHandlerService.class);
            
            try
            {
                handlerService.executeCommand("au.org.intersect.exsite9.commands.ReloadProjectCommand", null);
            }
            catch (NotDefinedException e)
            {
                LOG.error("Cannot execute reload project command", e);
            }
            catch (NotEnabledException e)
            {
                LOG.error("Cannot execute reload project command", e);
            }
            catch (NotHandledException e)
            {
                LOG.error("Cannot execute reload project command", e);
            }
        }
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public boolean isHandled()
    {
        return true;
    }

    @Override
    public void removeHandlerListener(final IHandlerListener handlerListener)
    {
    }

}
