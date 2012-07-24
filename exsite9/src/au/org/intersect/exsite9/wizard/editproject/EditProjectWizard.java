package au.org.intersect.exsite9.wizard.editproject;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Objects;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.service.ISchemaService;
import au.org.intersect.exsite9.wizard.newproject.EditOrCreateProjectWizardPage1;
import au.org.intersect.exsite9.wizard.newproject.EditOrCreateProjectWizardPage2;

public class EditProjectWizard extends Wizard
{
    private final EditOrCreateProjectWizardPage1 page1;
    private final EditOrCreateProjectWizardPage2 page2;

    private Project currentProject;

    private final IProjectService projectService;
    private final ISchemaService  schemaService;
    private final IGroupService groupService;
    private final IResearchFileService researchFileService;

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
        this.page2 = new EditOrCreateProjectWizardPage2("Edit Project", "Please amend the schema details to be used with your project", selectedProject.getSchema());

        this.projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
        this.schemaService = (ISchemaService) PlatformUI.getWorkbench().getService(ISchemaService.class);
        this.groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        this.researchFileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void addPages()
    {
        addPage(this.page1);
        addPage(this.page2);
    }

    @Override
    public boolean performFinish()
    {
        // Update fields
        this.currentProject = projectService.editProject(page1.getProjectFields(), currentProject.getId());

        // Update Schema
        final Schema currentSchema = currentProject.getSchema();

        final String localSchemaName = page2.getLocalSchemaName();
        final String localSchemaDescription = page2.getLocalSchemaDescription();
        final String localSchemaNamespaceURL = page2.getLocalSchemaNamespaceURL();
        final Schema importedSchema = page2.getImportedSchema();

        if (currentSchema.getLocal())
        {
            if (page2.getUseLocalSchema())
            {
                schemaService.updateSchema(currentSchema, localSchemaName, localSchemaDescription, localSchemaNamespaceURL);
            }
            else
            {
                // TODO: SPIT A WARNING
                schemaService.createImportedSchema(importedSchema);
                projectService.editProject(importedSchema, currentProject.getId());
                deleteSchema(currentSchema);
            }
        }
        else
        {
            if (page2.getUseLocalSchema())
            {
                // TODO: SPIT A WARNING
                final Schema localSchema = schemaService.createLocalSchema(localSchemaName, localSchemaDescription, localSchemaNamespaceURL);
                projectService.editProject(localSchema, currentProject.getId());
                deleteSchema(currentSchema);
            }
            else
            {
                if (!Objects.equal(currentSchema, importedSchema))
                {
                    // TODO: SPIT A WARNING
                    schemaService.createImportedSchema(importedSchema);
                    projectService.editProject(importedSchema, currentProject.getId());
                    deleteSchema(currentSchema);
                }
            }
        }

        return this.currentProject != null;
    }

    private void deleteSchema(final Schema schema)
    {
        // Iterate across all the Metadata Category -> Value mappings, remove all Group & File associations.
        for (final MetadataCategory mdc : schema.getMetadataCategories())
        {
            for (final MetadataValue mdv : mdc.getValues())
            {
                final List<ResearchFile> researchFiles = this.researchFileService.getResearchFilesWithAssociatedMetadata(mdc, mdv);
                for (final ResearchFile researchFile : researchFiles)
                {
                    this.researchFileService.disassociateMetadata(researchFile, mdc, mdv);
                }
                final List<Group> groups = this.groupService.getGroupsWithAssociatedMetadata(mdc, mdv);
                for (final Group group : groups)
                {
                    this.groupService.disassociateMetadata(group, mdc, mdv);
                }
            }
        }
        this.schemaService.removeSchema(schema);
    }

    public Project updateProject()
    {
        return this.currentProject;
    }
}
