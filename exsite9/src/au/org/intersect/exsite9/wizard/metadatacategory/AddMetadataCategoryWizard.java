package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IMetadataCategoryService;
import au.org.intersect.exsite9.service.IProjectService;

public class AddMetadataCategoryWizard extends Wizard
{
    private final AddMetadataCategoryWizardPage1 page1;
    private Project project;

    /**
     * Constructor
     * 
     * @param project
     */
    public AddMetadataCategoryWizard(Project project)
    {
        super();
        setNeedsProgressMonitor(true);
        this.project = project;
        page1 = new AddMetadataCategoryWizardPage1(project);
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
        final List<String> values = page1.getMetadataCategoryValues();

        // Persist the new metadata category.
        final IMetadataCategoryService metadataCategoryService = (IMetadataCategoryService) PlatformUI.getWorkbench().getService(IMetadataCategoryService.class);
        final MetadataCategory newCategory = metadataCategoryService.createNewMetadataCategory(categoryTitle, values);

        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
        projectService.addMetadataCategoryToProject(this.project, newCategory);

        return this.project != null;
    }

    public Project getProjectWithUpdatedMetadataCategories()
    {
        return this.project;
    }

}
