/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.ResearchFileSearchDefinition;

/**
 * Provides access to {@link SearchService}.
 */
public interface ISearchService
{
    List<ResearchFile> getResearchFilesUsingSearchTerm(String searchTerm, ResearchFileSearchDefinition selectedField, Project currentProject);
}
