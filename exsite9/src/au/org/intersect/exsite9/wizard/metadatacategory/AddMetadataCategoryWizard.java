package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;
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
        String categoryTitle = page1.getMetadataCategoryName();
        List<String> valuesSet = page1.getMetadataCategoryValues();
        MetadataCategory newCategory = new MetadataCategory(categoryTitle);
        newCategory.setValues(valuesSet);

        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(
                IProjectService.class);
        
        projectService.addMetadataCategoryToProject(project, newCategory);
        return this.project != null;
    }

    public Project getProjectWithUpdatedMetadataCategories()
    {
        return this.project;
    }

}
