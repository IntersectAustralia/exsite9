package au.org.intersect.exsite9.wizard.editproject;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.wizard.newproject.EditOrCreateProjectWizardPage1;

public class EditProjectWizard extends Wizard
{
    private final EditOrCreateProjectWizardPage1 page1;

    private Project currentProject;

    /**
     * Constructor
     * 
     * @param selectedProject
     */
    public EditProjectWizard(final Project selectedProject)
    {
        super();
        setNeedsProgressMonitor(true);
        this.currentProject = selectedProject;
        this.page1 = new EditOrCreateProjectWizardPage1("Edit Project", "Please amend the details of your project",
                selectedProject.getName(), selectedProject.getOwner(), selectedProject.getDescription());
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void addPages()
    {
        addPage(this.page1);
    }

    @Override
    public boolean performFinish()
    {
        final String projectName = page1.getProjectName();
        final String projectOwner = page1.getProjectOwner();
        final String projectDescription = page1.getProjectDescription();

        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(
                IProjectService.class);
        this.currentProject = projectService.editProject(projectName, projectOwner, projectDescription,
                currentProject.getId());
        return this.currentProject != null;
    }

    public Project updateProject()
    {
        return this.currentProject;
    }

}
