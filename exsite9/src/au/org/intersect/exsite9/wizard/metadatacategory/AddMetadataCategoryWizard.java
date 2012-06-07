package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        String categoryTitle = page1.categoryNameField.getContents();
        String[] metadataValues = page1.metadataValuesList.getItems();
        Set<String> valuesSet = new HashSet<String>(Arrays.asList(metadataValues));
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
