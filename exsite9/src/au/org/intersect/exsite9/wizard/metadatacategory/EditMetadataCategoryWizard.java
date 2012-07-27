/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IResearchFileService;

/**
 * Wizard used to Edit a Metadata Category.
 */
public final class EditMetadataCategoryWizard extends Wizard
{
    private final Schema schema;

    private final ListMetadataCategoriesWizardPage listCategoriesPage;
    private final AddMetadataCategoryWizardPage1 editCategoryPage;

    public EditMetadataCategoryWizard(final Schema schema)
    {
        super();
        setNeedsProgressMonitor(true);
        this.schema = schema;
        this.listCategoriesPage = new ListMetadataCategoriesWizardPage("Edit Metadata Category", "Choose a metadata category to edit.", this.schema, false);
        this.editCategoryPage = new AddMetadataCategoryWizardPage1("Edit Metadata Category",
    		"Edit the details of the metadata category you have selected", schema, null, Collections.<MetadataValue>emptyList());
    }

    @Override
    public void addPages()
    {
        addPage(this.listCategoriesPage);
        addPage(this.editCategoryPage);
    }

    @Override
    public boolean performFinish()
    {
        final IMetadataCategoryService metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench().getService(IMetadataCategoryService.class);
        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        final IResearchFileService fileService = (IResearchFileService) PlatformUI.getWorkbench().getService(IResearchFileService.class);

        final String categoryTitle = editCategoryPage.getMetadataCategoryName();
        final MetadataCategoryUse categoryUse = editCategoryPage.getMetadataCategoryUse();
        final List<MetadataValue> values = editCategoryPage.getMetadataCategoryValues();

        // Update the modified medata category
        final MetadataCategory metadataCategory = listCategoriesPage.getSelectedMetadataCategory();
        if (metadataCategory == null)
        {
            return true;
        }

        for (final ResearchFile assignedFile : editCategoryPage.getAssignedFiles())
        {
            fileService.disassociateMultipleMetadataValues(assignedFile, metadataCategory, editCategoryPage.getValuesToBeDisassociated());
        }

        for (final Group assignedGroup : editCategoryPage.getAssignedGroups())
        {
            groupService.disassociateMultipleMetadataValues(assignedGroup, metadataCategory, editCategoryPage.getValuesToBeDisassociated());
        }

        metadataCategoryService.updateMetadataCategory(metadataCategory, categoryTitle, categoryUse, values);
        return true;
    }
}
