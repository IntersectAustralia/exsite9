package au.org.intersect.exsite9.wizard.listfolders;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectManager;

public class ListFoldersWizard extends Wizard
{

    /**
     * @{inheritDoc}
     */
    @Override
    public void addPages()
    {
        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project project = projectManager.getCurrentProject();
        final List<Folder> folders = project.getFolders();
        
        ListFoldersWizardPage1 page1 = new ListFoldersWizardPage1(folders);
        
        addPage(page1);
    }
    
    @Override
    public boolean performFinish()
    {
        // TODO Auto-generated method stub
        return false;
    }

}
