package au.org.intersect.exsite9.wizard.listfolders;

import java.util.List;

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
     * @{inheritDoc}
     */
    @Override
    public void addPages()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        final List<Folder> folders = project.getFolders();
        
        this.page1 = new ListFoldersWizardPage1(folders);
        
        addPage(page1);
    }
    
    @Override
    public boolean performFinish()
    {
        final List<String> modifiedFolderList = this.page1.getFolderList();
        
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        
        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
        
        Project updatedproject = projectService.removeFoldersFromProject(project, modifiedFolderList);
        projectManager.setCurrentProject(updatedproject);
        
        return true;
    }

}
