/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;

public class ProjectService implements IProjectService
{
    private final ProjectDAO projectDAO;
    private final FolderDAO folderDAO;
    
    public ProjectService(final ProjectDAO projectDAO,
                          final FolderDAO folderDAO)
    {
        this.projectDAO = projectDAO;
        this.folderDAO = folderDAO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project createProject(final String name, final String owner, final String description)
    {
        Project project = new Project(name, owner, description);
        projectDAO.createProject(project);
        return project;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void mapFolderToProject(final Project project, final Folder folder)
    {
        folderDAO.createFolder(folder);
        project.getFolders().add(folder);
        projectDAO.updateProject(project);
    }
}
