/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;

public interface IProjectService
{
    /**
     * Creates a project.
     * @param name The name of the project.
     * @param owner The owner of the project.
     * @param description The description of the project.
     * @return The newly created project.
     */
    Project createProject(final String name, final String owner, final String description);

    /**
     * Maps a folder to a project.
     * @param project The project.
     * @param folder The folder to map to the project.
     */
    void mapFolderToProject(final Project project, final Folder folder);
    
    /**
     * finds all projects in the database.
     * @return list of projects
     */
    public List<Project> getAllProjects();
    
    /**
     * Edit a project.
     * @param name The updated name of the project.
     * @param owner The updated owner of the project.
     * @param description The updated description of the project.
     * @param id of project to update 
     * @return The updated project.
     */
    Project editProject(final String name, final String owner, final String description, final Long id);

    void addMetadataCategoryToProject(final Project project, final MetadataCategory metadataCategory);
    
    Project findProjectById(Long id);
}
