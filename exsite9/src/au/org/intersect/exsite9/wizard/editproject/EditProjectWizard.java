package au.org.intersect.exsite9.wizard.editproject;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
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
                new ProjectFieldsDTO(selectedProject.getName(), selectedProject.getOwner(),
                        selectedProject.getDescription(), selectedProject.getCollectionType(),
                        selectedProject.getRightsStatement(), selectedProject.getAccessRights(),
                        selectedProject.getLicence(), selectedProject.getIdentifier(), selectedProject.getSubject(),
                        selectedProject.getElectronicLocation(), selectedProject.getPhysicalLocation(),
                        selectedProject.getPlaceOrRegionName(), selectedProject.getLatitudeLongitude(),
                        selectedProject.getDatesOfCapture(), selectedProject.getCitationInformation(),
                        selectedProject.getRelatedParty(), selectedProject.getRelatedActivity(),
                        selectedProject.getRelatedInformation()));
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
        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(
                IProjectService.class);
        this.currentProject = projectService.editProject(page1.getProjectFields(),
                currentProject.getId());
        return this.currentProject != null;
    }

    public Project updateProject()
    {
        return this.currentProject;
    }

}
