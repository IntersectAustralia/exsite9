/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.listfolders;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IProjectService;

/**
 * The wizard used to display the list of existing watched folders via {@link ListFoldersWizardPage1}
 */
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
        setWindowTitle("Folders");

        this.page1 = new ListFoldersWizardPage1(folders);

        addPage(page1);
    }

    @Override
    public boolean performFinish()
    {
        final List<String> deletedFolderList = this.page1.getDeletedFolderList();
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

                // TODO: What if the new folder is a parent folder of an existing watched folder
                
                // do updates
                projectService.updateFolderPath(folderAndPathEntry.getKey().getId(), newFileForFolder);
            }
        }

        // remove any existing folders that have been chosen to be removed
        if (!deletedFolderList.isEmpty())
        {
            projectService.removeFoldersFromProject(project, deletedFolderList);
        }

        return true;
    }

}
