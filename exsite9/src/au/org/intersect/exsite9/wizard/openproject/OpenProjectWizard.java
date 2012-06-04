package au.org.intersect.exsite9.wizard.openproject;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectService;

public class OpenProjectWizard extends Wizard
{
    private final OpenProjectWizardPage1 page1 = new OpenProjectWizardPage1();
    final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);

    private Project selectedProject;

    /**
     * Constructor 
     */
    public OpenProjectWizard()
    {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addPages()
    {
        page1.projectItems = projectService.getAllProjects();
        addPage(this.page1);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        this.selectedProject = page1.selectedProject;
        return this.selectedProject != null;
    }

    public Project getSelectedProject()
    {
        // TODO Auto-generated method stub
        return this.selectedProject;
    }
}
