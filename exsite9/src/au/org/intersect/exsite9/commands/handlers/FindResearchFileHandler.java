package au.org.intersect.exsite9.commands.handlers;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.NewFilesGroup;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.service.IResearchFileService;

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

        // Watched folder checking
        for (Folder existingFolder : currentProject.getFolders())
        {
            if (existingFolder.getFolder().getAbsolutePath().equalsIgnoreCase(parentPathOfNewFile))
            { // means the new location is in a folder that is already being watched

                for (ResearchFile existingResearchFile : existingFolder.getFiles())
                {
                    if (existingResearchFile.getFile().equals(newFileObject))
                    {
                        boolean confirmation = MessageDialog.openConfirm(shell, "", "The location you have chosen already contains a file with the same name that is already in the system, by proceeding the file and any associated metadata will be replaced by your selected file.");
                        if (confirmation)
                        {
                            existingResearchFile.getMetadataAssociations().clear();
                            existingResearchFile.getMetadataAssociations().addAll(
                                    ((ResearchFile) selectionObject).getMetadataAssociations());

                            if (existingResearchFile.getParentGroup() instanceof NewFilesGroup)
                            {
                                existingResearchFile.setParentGroup(((ResearchFile) selectionObject).getParentGroup());
                            }

                            // remove the selected file from the submission packages and add the existing one and delete
                            // the selected file from the system
                            projectService.replaceResearchFileInSubmissionPackageAndDeleteReplacedFile(
                                    ((ResearchFile) selectionObject).getId(), existingResearchFile.getId());
                        }
                        return null;
                    }
                }
                // if it hits this point it means that the folder is already watched but the file is not currently in
                // the system
                ((ResearchFile) selectionObject).setFile(newFileObject);
                researchFileService.updateResearchFile((ResearchFile) selectionObject);
                return null;
            }
        }

        // non-watched folder
        boolean startWatchingFolder = MessageDialog.openConfirm(shell, "Watch Folder?", "The file you selected is not currently within a watched folder, to change the location of this file to your selected directory the system must begin watching that directory, do you want to do this?");

        if (startWatchingFolder)
        {
            ((ResearchFile) selectionObject).setFile(newFileObject);
            researchFileService.updateResearchFile((ResearchFile) selectionObject);
            projectService.mapFolderToProject(currentProject, new Folder(new File(parentPathOfNewFile)));
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
