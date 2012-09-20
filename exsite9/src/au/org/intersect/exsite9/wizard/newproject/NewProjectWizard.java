/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newproject;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.FieldOfResearch;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.service.ISchemaService;

/**
 * The Wizard used to create a new project
 */
public final class NewProjectWizard extends Wizard
{
    private final EditOrCreateProjectWizardPage1 page1;
    private final EditOrCreateProjectWizardPage2 page2;
    private final EditOrCreateProjectWizardPage3 page3;

    private Project newProject;

    /**
     * Constructor
     */
    public NewProjectWizard()
    {
        super();
        setNeedsProgressMonitor(true);
        final ProjectFieldsDTO projectFieldsDTO = new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", null, "", "", "", "", "");
        page1 = new EditOrCreateProjectWizardPage1("New Project", "Please enter the details of your new project.", projectFieldsDTO);
        page2 = new EditOrCreateProjectWizardPage2("New Project", "Please enter the details of your new project.", projectFieldsDTO);
        page3 = new EditOrCreateProjectWizardPage3("New Project", "Please enter the schema details to be used in your new project.", null);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void addPages()
    {
        addPage(this.page1);
        addPage(this.page2);
        addPage(this.page3);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public boolean performFinish()
    {
        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
        final ISchemaService schemaService = (ISchemaService) PlatformUI.getWorkbench().getService(ISchemaService.class);

        final Schema schema;
        if (page3.getUseLocalSchema())
        {
            final String schemaName = page3.getLocalSchemaName();
            final String schemaDescription = page3.getLocalSchemaDescription();
            final String schemaNamespaceURL = page3.getLocalSchemaNamespaceURL();
            schema = schemaService.createLocalSchema(schemaName, schemaDescription, schemaNamespaceURL);
        }
        else
        {
            schema = page3.getImportedSchema();
            schemaService.createImportedSchema(schema);
        }

        final ProjectFieldsDTO projectFieldsDTO = createProjectFieldsDTO(page1, page2);
        this.newProject = projectService.createProject(projectFieldsDTO, schema);
        return this.newProject != null;
    }

    public Project getNewProject()
    {
        return this.newProject;
    }

    public static ProjectFieldsDTO createProjectFieldsDTO(final EditOrCreateProjectWizardPage1 page1, final EditOrCreateProjectWizardPage2 page2)
    {
        final String name = page1.getProjectName();
        final String owner = page1.getOwner();
        final String institution = page1.getInstitution();
        final String email = page1.getEmail();
        final String description = page1.getProjectDescription();
        final String collectionType = page1.getCollectionType();
        final String rightsStatement = page1.getRightsStatement();
        final String accessRights = page1.getAccessRights();
        final String license = page1.getLicense();
        final String identifier = page1.getIdentifier();
        final String subject = page1.getSubject();
        final String electronicLocation = page2.getElectronicLocation();
        final String physicalLocation = page2.getPhysicalLocation();
        final String placeOrRegionName = page2.getPlaceOrRegionName();
        final String geographicalCoverage = page2.getGeographicalCoverage();
        final String datesOfCapture = page2.getDatesOfCapture();
        final String citationInformation = page2.getCitationInformation();
        final String countries = page2.getCountries();
        final String languages = page2.getLanguages();
        final FieldOfResearch fieldOfResearch = page2.getFieldOfResearch();
        final String fundingBody = page2.getFundingBody();
        final String grantID = page2.getGrantID();
        final String relatedParty = page2.getRelatedParty();
        final String relatedGrant = page2.getRelatedActivity();
        final String relatedInformation = page2.getRelatedInformation();

        final ProjectFieldsDTO projectFieldsDTO = new ProjectFieldsDTO(name, owner, institution, email, description, collectionType, rightsStatement,
            accessRights, license, identifier, subject, electronicLocation, physicalLocation, placeOrRegionName, geographicalCoverage, datesOfCapture,
            citationInformation, countries, languages, fieldOfResearch, fundingBody, grantID, relatedParty, relatedGrant, relatedInformation);
        return projectFieldsDTO;
    }
}
