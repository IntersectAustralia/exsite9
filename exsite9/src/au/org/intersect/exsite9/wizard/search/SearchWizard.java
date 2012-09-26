/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.search;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.ResearchFileSearchDefinition;
import au.org.intersect.exsite9.service.ISearchService;

/**
 * Wizard used to allow a user to do a search, the user is shown the wizard page {@link SearchWizardPage1}
 */
public class SearchWizard extends Wizard
{
    private SearchWizardPage1 page1;
    private Project currentProject;
    List<ResearchFile> searchResults;

    public SearchWizard(Project selectedProject)
    {
        super();
        this.currentProject = selectedProject;
        setNeedsProgressMonitor(true);
        setWindowTitle("Search");
        this.page1 = new SearchWizardPage1(ResearchFileSearchDefinition.toArray());
    }

    @Override
    public void addPages()
    {
        addPage(this.page1);
    }

    @Override
    public boolean performFinish()
    {
        final ResearchFileSearchDefinition selectedField = page1.getSearchCategory();
        final String searchTerm = page1.getSearchTerm();
        final ISearchService searchService = (ISearchService) PlatformUI.getWorkbench()
                .getService(ISearchService.class);
        this.searchResults = searchService.getResearchFilesUsingSearchTerm(searchTerm, selectedField, this.currentProject);
        return true;
    }
    
    
    public List<ResearchFile> getSearchResults()
    {
        return searchResults;
    }
}
