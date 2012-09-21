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
import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
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
        setWindowTitle("Edit Metadata Category");
        this.schema = schema;
        this.listCategoriesPage = new ListMetadataCategoriesWizardPage("Edit Metadata Category", "Choose a metadata category to edit.", this.schema, false);
        this.editCategoryPage = new AddMetadataCategoryWizardPage1("Edit Metadata Category",
    		"Edit the details of the metadata category you have selected", schema, null, Collections.<MetadataValue>emptyList(), Collections.<MetadataAttributeValue>emptyList());
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
        final String categoryDescription = editCategoryPage.getCategoryDescription();
        final boolean inextensible = editCategoryPage.getIsInextensible();

        // Update the modified metadata category
        final MetadataCategory metadataCategory = listCategoriesPage.getSelectedMetadataCategory();
        if (metadataCategory == null)
        {
            return true;
        }

        for (final ResearchFile assignedFile : editCategoryPage.getAssignedFiles())
        {
            fileService.disassociateMultipleMetadataValues(assignedFile, metadataCategory, editCategoryPage.getMetadataValuesToBeDisassociated());
        }

        for (final Group assignedGroup : editCategoryPage.getAssignedGroups())
        {
            groupService.disassociateMultipleMetadataValues(assignedGroup, metadataCategory, editCategoryPage.getMetadataValuesToBeDisassociated());
        }

        for (final MetadataAttributeValue mdav : editCategoryPage.getMetadataAttributeValuesToBeDisassociated())
        {
            groupService.disassociateMetadataAttributeValue(metadataCategory, mdav);
            fileService.disassociateMetadataAttributeValue(metadataCategory, mdav);
        }

        final MetadataAttribute oldMDA = metadataCategory.getMetadataAttribute();
        final MetadataAttribute newMDA;

        if (oldMDA != null)
        {
            final String attrName = editCategoryPage.getMetadataAttributeName();
            if (!attrName.isEmpty())
            {
                newMDA = oldMDA;
                newMDA.setName(editCategoryPage.getMetadataAttributeName());
                newMDA.setMetadataAttributeValues(editCategoryPage.getMetadataAttributeValues());
            }
            else
            {
                newMDA = null;
            }
        }
        else
        {
            final String attrName = editCategoryPage.getMetadataAttributeName();
            if (!attrName.isEmpty())
            {
                newMDA = new MetadataAttribute(attrName, editCategoryPage.getMetadataAttributeValues());
            }
            else
            {
                newMDA = null;
            }
        }

        metadataCategoryService.updateMetadataCategory(metadataCategory, categoryTitle, categoryDescription, categoryUse, inextensible, values, newMDA);
        return true;
    }
}
