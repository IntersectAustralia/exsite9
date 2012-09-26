/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.editproject;

import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.wizard.importmetadataschema.MetadataSchemaEditingWizard;
import au.org.intersect.exsite9.wizard.newproject.EditOrCreateProjectWizardPage1;
import au.org.intersect.exsite9.wizard.newproject.EditOrCreateProjectWizardPage2;
import au.org.intersect.exsite9.wizard.newproject.NewProjectWizard;

/**
 * The wizard that is used to edit an existing project. It creates a {@link EditOrCreateProjectWizardPage1} and {@link EditOrCreateProjectWizardPage2}
 */
public class EditProjectWizard extends MetadataSchemaEditingWizard
{
    private final EditOrCreateProjectWizardPage1 page1;
    private final EditOrCreateProjectWizardPage2 page2;

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
        setWindowTitle("Edit Project");
        final ProjectFieldsDTO projectFieldsDTO = new ProjectFieldsDTO(selectedProject.getName(), selectedProject.getOwner(),
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
                selectedProject.getRelatedInformation());
        this.page1 = new EditOrCreateProjectWizardPage1("Edit Project", "Please amend the details of your project", projectFieldsDTO);
        this.page2 = new EditOrCreateProjectWizardPage2("Edit Project", "Please amend the details of your project", projectFieldsDTO);

        this.projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void addPages()
    {
        addPage(this.page1);
        addPage(this.page2);
        super.addPages();
    }

    @Override
    public boolean performFinish()
    {
        // Update project details
        final ProjectFieldsDTO projectFieldsDTO = NewProjectWizard.createProjectFieldsDTO(page1, page2);
        super.currentProject = projectService.editProject(projectFieldsDTO, currentProject.getId());

        // Update schema details
        return super.performFinish();
    }

    public Project updateProject()
    {
        return this.currentProject;
    }
}
