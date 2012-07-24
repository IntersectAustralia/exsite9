/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.importmetadataschema;

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
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IProjectService;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.service.ISchemaService;
import au.org.intersect.exsite9.wizard.newproject.EditOrCreateProjectWizardPage2;

/**
 * A Wizard that changes Metadata Schema.
 * A convenience class to avoid code duplication between the Edit Project Wizard and the Import Schema Wizard.
 */
public abstract class MetadataSchemaEditingWizard extends Wizard
{
    private final EditOrCreateProjectWizardPage2 schemaEditPage;

    private final IProjectService projectService;
    private final ISchemaService  schemaService;
    private final IGroupService groupService;
    private final IResearchFileService researchFileService;

    protected Project currentProject;

    public MetadataSchemaEditingWizard(final Project selectedProject, final String title)
    {
        this.schemaEditPage = new EditOrCreateProjectWizardPage2(title, "Please amend the schema details to be used with your project", selectedProject.getSchema());
        this.currentProject = selectedProject;

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
        addPage(this.schemaEditPage);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        // Update Schema
        final Schema currentSchema = currentProject.getSchema();

        final String localSchemaName = schemaEditPage.getLocalSchemaName();
        final String localSchemaDescription = schemaEditPage.getLocalSchemaDescription();
        final String localSchemaNamespaceURL = schemaEditPage.getLocalSchemaNamespaceURL();
        final Schema importedSchema = schemaEditPage.getImportedSchema();

        if (currentSchema.getLocal())
        {
            if (schemaEditPage.getUseLocalSchema())
            {
                schemaService.updateSchema(currentSchema, localSchemaName, localSchemaDescription, localSchemaNamespaceURL);
            }
            else
            {
                schemaService.createImportedSchema(importedSchema);
                projectService.editProject(importedSchema, currentProject.getId());
                deleteSchema(currentSchema);
            }
        }
        else
        {
            if (schemaEditPage.getUseLocalSchema())
            {
                final Schema localSchema = schemaService.createLocalSchema(localSchemaName, localSchemaDescription, localSchemaNamespaceURL);
                projectService.editProject(localSchema, currentProject.getId());
                deleteSchema(currentSchema);
            }
            else
            {
                if (!Objects.equal(currentSchema, importedSchema))
                {
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
}
