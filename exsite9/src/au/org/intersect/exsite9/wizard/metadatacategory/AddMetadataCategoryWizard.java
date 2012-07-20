package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategory;
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
                    "Please enter the details of the metadata category you wish to create", project, null,
                    new ArrayList<MetadataValue>());
        }
        else
        {
            page1 = new AddMetadataCategoryWizardPage1("Edit Metadata Category",
                    "Edit the details of the metadata category you have selected", project, metadataCategory,
                    metadataCategory.getValues());
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
        final MetadataCategoryUse categoryUse = page1.getMetadataCategoryUse();
        final List<MetadataValue> values = page1.getMetadataCategoryValues();

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
            final MetadataCategory newCategory = metadataCategoryService.createNewMetadataCategory(categoryTitle, categoryUse,
                    values);
            schemaService.addMetadataCategoryToSchema(this.project.getSchema(), newCategory);
        }
        else
        {
            metadataCategoryService.updateMetadataCategory(this.metadataCategory, categoryTitle, categoryUse,
                    page1.getMetadataCategoryValues());
        }

        return this.project != null;
    }

    public Project getProjectWithUpdatedMetadataCategories()
    {
        return this.project;
    }

}
