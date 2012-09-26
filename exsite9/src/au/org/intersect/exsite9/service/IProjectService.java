/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.io.File;
import java.util.List;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Provides access to {@link ProjectService}.
 */
public interface IProjectService
{
    /**
     * Creates a project.
     * @param projectFields the various fields that can be inputed when creating a project.
     * @param schema the schema to use for the project.
     * @return The newly created project.
     */
    Project createProject(final ProjectFieldsDTO projectFields, final Schema schema);

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
     * @param deletedFolderList The list of folders to be removed
     */
    public Project removeFoldersFromProject(final Project project, final List<String> deletedFolderList);
    
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

    /**
     * Edit a project
     * @param schema The new schema to assign to the project.
     * @param id of the project to update.
     * @return The updated project.
     */
    Project editProject(final Schema schema, final Long id);

    
    Project findProjectById(Long id);
    
    /**
     * Updates the path of a folder within the given project.
     * @param folderId
     * @param newFileForFolder
     */
    void updateFolderPath(long folderId, File newFileForFolder);
    
    /**
     * Builds the xml for the project's metadata schema
     * @param project The project
     * @return the xml
     */
    String buildMetadataSchemaXML(final Project project);

     /** Updates any submission packages that a specified research file is in and deletes the file from the system
     * @param fileToBeReplacedId
     */
    void removeResearchFileFromSystem(long fileToBeRemovedId);
}
