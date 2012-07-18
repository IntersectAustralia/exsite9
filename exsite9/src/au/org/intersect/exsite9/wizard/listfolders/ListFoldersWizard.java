package au.org.intersect.exsite9.wizard.listfolders;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IProjectService;

public class ListFoldersWizard extends Wizard
{

    private ListFoldersWizardPage1 page1;

    /**
     * @{inheritDoc
     */
    @Override
    public void addPages()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(
                IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        final List<Folder> folders = project.getFolders();

        this.page1 = new ListFoldersWizardPage1(folders);

        addPage(page1);
    }

    @Override
    public boolean performFinish()
    {
        final List<Folder> deletedFolderList = this.page1.getDeletedFolderList();
        final Map<Folder, String> foldersThatNeedToBeUpdated = page1.getFoldersAndTheirUpdatedPaths();

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(
                IProjectManager.class);
        final Project project = projectManager.getCurrentProject();

        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(
                IProjectService.class);

        if (!foldersThatNeedToBeUpdated.isEmpty())
        {
            for (Map.Entry<Folder, String> folderAndPathEntry : foldersThatNeedToBeUpdated.entrySet())
            {
                final File newFileForFolder = new File(folderAndPathEntry.getValue());

                if (!newFileForFolder.exists() || !newFileForFolder.isDirectory() || !newFileForFolder.canRead())
                {
                    MessageDialog.openError(null, "Error", "Provided folder does not exist or is not readable.");
                    return false;
                }

                // check the new path isn't already a folder within the project
                // TODO : We need to deal with the case that a parent directory of this one has already been added.
                // OR this is a parent directory which contains directories that have already been added.
                for (Folder existingFolder : project.getFolders())
                {
                    if (existingFolder.getFolder().getAbsolutePath().equalsIgnoreCase(newFileForFolder.getAbsolutePath()))
                    {
                        MessageDialog.openError(null, "Error",
                                "The folder you chose is already assigned to the project.");
                        return false;
                    }
                }
                // do updates
                projectService.updateFolderPath(folderAndPathEntry.getKey().getId(),
                        newFileForFolder);
            }
        }

        //need to update the files in the folder but probably in the service

        //remove any existing folders that have been chosen to be removed
        if (!deletedFolderList.isEmpty())
        {
            projectService.removeFoldersFromProject(project, deletedFolderList);
        }

        return true;
    }

}
