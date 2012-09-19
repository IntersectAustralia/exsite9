package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.ResearchFileSearchDefinition;

public interface ISearchService
{
    List<ResearchFile> getResearchFilesUsingSearchTerm(String searchTerm, ResearchFileSearchDefinition selectedField, Project currentProject);
}
