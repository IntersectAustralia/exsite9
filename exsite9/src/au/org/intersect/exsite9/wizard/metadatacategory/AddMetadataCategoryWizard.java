package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.service.IGroupService;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IResearchFileService;
import au.org.intersect.exsite9.service.ISchemaService;

public class AddMetadataCategoryWizard extends Wizard
{
    private final AddMetadataCategoryWizardPage1 page1;

    private MetadataCategory metadataCategory;
    private Project project;

    /**
     * Constructor
     * 
     * @param project
     * @param metadataCategory
     *            can be null
     */
    public AddMetadataCategoryWizard(Project project, MetadataCategory metadataCategory)
    {
        super();
        setNeedsProgressMonitor(true);
        this.project = project;
        this.metadataCategory = metadataCategory;
        if (metadataCategory == null)
        {
            page1 = new AddMetadataCategoryWizardPage1("Add Metadata Category",
                    "Please enter the details of the metadata category you wish to create", project.getSchema(), null,
                    new ArrayList<MetadataValue>(), new ArrayList<MetadataAttributeValue>());
        }
        else
        {
            page1 = new AddMetadataCategoryWizardPage1("Edit Metadata Category",
                    "Edit the details of the metadata category you have selected", project.getSchema(), metadataCategory,
                    metadataCategory.getValues(), null);
        }
    }

    @Override
    public void addPages()
    {
        addPage(this.page1);
    }

    @Override
    public boolean performFinish()
    {
        final String categoryTitle = page1.getMetadataCategoryName();
        final String categoryDescription = page1.getCategoryDescription();
        final MetadataCategoryType categoryType = page1.getMetadataCategoryType();
        final MetadataCategoryUse categoryUse = page1.getMetadataCategoryUse();
        final boolean inextensible = page1.getIsInextensible();

        // Persist the new metadata category.
        final IMetadataCategoryService metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench()
                .getService(IMetadataCategoryService.class);
        final IGroupService groupService = (IGroupService) PlatformUI.getWorkbench().getService(IGroupService.class);
        final IResearchFileService fileService = (IResearchFileService) PlatformUI.getWorkbench().getService(
                IResearchFileService.class);
        final ISchemaService schemaService = (ISchemaService) PlatformUI.getWorkbench().getService(ISchemaService.class);

        for (final ResearchFile assignedFile : page1.getAssignedFiles())
        {
            fileService.disassociateMultipleMetadataValues(assignedFile, metadataCategory, page1.getValuesToBeDisassociated());
        }
        
        for (final Group assignedGroup : page1.getAssignedGroups())
        {
            groupService.disassociateMultipleMetadataValues(assignedGroup, metadataCategory, page1.getValuesToBeDisassociated());
        }
        
        if (this.metadataCategory == null)
        {
            final List<MetadataValue> metadataValues;
            final MetadataAttribute metadataAttribute;
            if (categoryType == MetadataCategoryType.CONTROLLED_VOCABULARY)
            {
                metadataValues = page1.getMetadataCategoryValues();
                metadataAttribute = null;
            }
            else
            {
                metadataValues = Collections.emptyList();
                final String metadataAttributeName = page1.getMetadataAttributeName();
                metadataAttribute = new MetadataAttribute(metadataAttributeName, page1.getMetadataAttributeValues());
            }
            final MetadataCategory newCategory = metadataCategoryService.createNewMetadataCategory(categoryTitle, categoryType, categoryUse, inextensible, false, metadataValues, metadataAttribute);
            schemaService.addMetadataCategoryToSchema(this.project.getSchema(), newCategory);
        }
        else
        {
            metadataCategoryService.updateMetadataCategory(this.metadataCategory, categoryTitle, categoryDescription, categoryUse, inextensible, page1.getMetadataCategoryValues());
        }

        return this.project != null;
    }

    public Project getProjectWithUpdatedMetadataCategories()
    {
        return this.project;
    }

}
