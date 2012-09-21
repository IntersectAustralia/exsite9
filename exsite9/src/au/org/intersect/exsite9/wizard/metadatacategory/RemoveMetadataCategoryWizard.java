/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.service.ISchemaService;

/**
 * Wizard use to remove metadata categories.
 */
public final class RemoveMetadataCategoryWizard extends Wizard
{
    private final Schema schema;

    private final ListMetadataCategoriesWizardPage listCategoriesPage;

    public RemoveMetadataCategoryWizard(final Schema schema)
    {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("Delete Metadata Category");
        this.schema = schema;
        this.listCategoriesPage = new ListMetadataCategoriesWizardPage("Delete Metadata Category", "Select metadata categories to be deleted.", this.schema, true);
    }

    @Override
    public void addPages()
    {
        addPage(this.listCategoriesPage);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        final IMetadataCategoryService metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench().getService(IMetadataCategoryService.class);
        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        final IResearchFileService fileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);
        final ISchemaService schemaService = (ISchemaService) PlatformUI.getWorkbench().getService(ISchemaService.class);

        final List<MetadataCategory> categoriesToDelete = listCategoriesPage.getMetadataCategoriesToDelete();
        for (final MetadataCategory mdc : categoriesToDelete)
        {
            for (final MetadataValue mdv : mdc.getValues())
            {
                final List<ResearchFile> researchFiles = fileService.getResearchFilesWithAssociatedMetadata(mdc, mdv);
                for (final ResearchFile researchFile : researchFiles)
                {
                    fileService.disassociateMetadata(researchFile, mdc, mdv);
                }
                final List<Group> groups = groupService.getGroupsWithAssociatedMetadata(mdc, mdv);
                for (final Group group : groups)
                {
                    groupService.disassociateMetadata(group, mdc, mdv);
                }
            }
            schemaService.removeMetadataCategoryFromSchema(schema, mdc);
            metadataCategoryService.deleteMetadataCategory(mdc);
        }
        return true;
    }

}
