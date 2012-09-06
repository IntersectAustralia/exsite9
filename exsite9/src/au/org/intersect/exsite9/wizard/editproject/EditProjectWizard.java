package au.org.intersect.exsite9.wizard.editproject;

import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.wizard.importmetadataschema.MetadataSchemaEditingWizard;
import au.org.intersect.exsite9.wizard.newproject.EditOrCreateProjectWizardPage1;

public class EditProjectWizard extends MetadataSchemaEditingWizard
{
    private final EditOrCreateProjectWizardPage1 page1;

    private final IProjectService projectService;

    /**
     * Constructor
     * 
     * @param selectedProject
     */
    public EditProjectWizard(final Project selectedProject)
    {
        super(selectedProject, "Edit Project");
        setNeedsProgressMonitor(true);
        this.page1 = new EditOrCreateProjectWizardPage1("Edit Project", "Please amend the details of your project",
                new ProjectFieldsDTO(selectedProject.getName(), selectedProject.getOwner(),
                        selectedProject.getInstitution(), selectedProject.getEmail(),
                        selectedProject.getDescription(), selectedProject.getCollectionType(),
                        selectedProject.getRightsStatement(), selectedProject.getAccessRights(),
                        selectedProject.getLicence(), selectedProject.getIdentifier(), selectedProject.getSubject(),
                        selectedProject.getElectronicLocation(), selectedProject.getPhysicalLocation(),
                        selectedProject.getPlaceOrRegionName(), selectedProject.getGeographicalCoverage(),
                        selectedProject.getDatesOfCapture(), selectedProject.getCitationInformation(),
                        selectedProject.getCountries(), selectedProject.getLanguages(), selectedProject.getFieldOfResearch(),
                        selectedProject.getFundingBody(), selectedProject.getGrantID(),
                        selectedProject.getRelatedParty(), selectedProject.getRelatedGrant(),
                        selectedProject.getRelatedInformation()));

        this.projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void addPages()
    {
        addPage(this.page1);
        super.addPages();
    }

    @Override
    public boolean performFinish()
    {
        // Update project details
        super.currentProject = projectService.editProject(page1.getProjectFields(), currentProject.getId());

        // Update schema details
        return super.performFinish();
    }

    public Project updateProject()
    {
        return this.currentProject;
    }
}
