/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;
import java.util.Map;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

public interface IProjectService
{
    /**
     * Creates a project.
     * @param projectFields the various fields that can be inputed when creating a project.
     * @return The newly created project.
     */
    Project createProject(final ProjectFieldsDTO projectFields);

    /**
     * Maps a folder to a project.
     * @param project The project.
     * @param folder The folder to map to the project.
     */
    void mapFolderToProject(final Project project, final Folder folder);
    
    /**
     * Remove folders from a project. This will also remove all files added to the project
     * because they were from a removed folder.
     * @param project The current project
     * @param folders The list of folder paths minus the ones to remove
     */
    public Project removeFoldersFromProject(final Project project, final List<String> folders);
    
    /**
     * finds all projects in the database.
     * @return list of projects
     */
    public List<Project> getAllProjects();
    
    /**
     * Edit a project.
     * @param projectFields the various fields that can be inputed when editing a project.
     * @param id of project to update 
     * @return The updated project.
     */
    Project editProject(final ProjectFieldsDTO projectFields, final Long id);

    void addMetadataCategoryToProject(final Project project, final MetadataCategory metadataCategory);
    
    Project findProjectById(Long id);
}
