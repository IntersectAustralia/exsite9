package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.IProjectService;

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
        final List<MetadataValue> values = page1.getMetadataCategoryValues();

        // Persist the new metadata category.
        final IMetadataCategoryService metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench()
                .getService(IMetadataCategoryService.class);
        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(
                IProjectService.class);

        if (this.metadataCategory == null)
        {
            final MetadataCategory newCategory = metadataCategoryService.createNewMetadataCategory(categoryTitle,
                    values);
            projectService.addMetadataCategoryToProject(this.project, newCategory);
        }
        else
        {
            metadataCategoryService.updateMetadataCategory(this.metadataCategory, categoryTitle,
                    page1.getMetadataCategoryValues());
            this.project = projectService.findProjectById(this.project.getId());

            // We need to do this because we do not directly update Project's object model, so we need to get a fresh object from the db.
            final IProjectManager projectManagerService = (IProjectManager) PlatformUI.getWorkbench().getService(
                    IProjectManager.class);
            projectManagerService.setCurrentProject(this.project);
        }

        return this.project != null;
    }

    public Project getProjectWithUpdatedMetadataCategories()
    {
        return this.project;
    }

}
