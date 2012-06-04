package au.org.intersect.exsite9.wizard.openproject;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectService;

public class OpenProjectWizard extends Wizard
{
    private OpenProjectWizardPage1 page1;
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
        final List<Project> availableProjects = projectService.getAllProjects();
        page1 = new OpenProjectWizardPage1(availableProjects);
        addPage(this.page1);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        this.selectedProject = page1.getSelectedProject();
        return this.selectedProject != null;
    }

    public Project getSelectedProject()
    {
        return this.selectedProject;
    }
}
