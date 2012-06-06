/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import javax.persistence.EntityManager;
import java.util.List;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;

public class ProjectService implements IProjectService
{
    private final ExSite9EntityManagerFactory entityManagerFactory;
    private final ProjectDAOFactory projectDAOFactory;
    private final FolderDAOFactory folderDAOFactory;

    public ProjectService(final ExSite9EntityManagerFactory entityManagerFactory,
            final ProjectDAOFactory projectDAOFactory, final FolderDAOFactory folderDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.projectDAOFactory = projectDAOFactory;
        this.folderDAOFactory = folderDAOFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project createProject(final String name, final String owner, final String description)
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            Project project = new Project(name, owner, description);
            projectDAO.createProject(project);
            return project;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mapFolderToProject(final Project project, final Folder folder)
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            FolderDAO folderDAO = folderDAOFactory.createInstance(em);
            folderDAO.createFolder(folder);
            project.getFolders().add(folder);
            projectDAO.updateProject(project);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public List<Project> getAllProjects()
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            return projectDAO.findAllProjects();
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public Project editProject(String name, String owner, String description, Long id)
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
            Project project = projectDAO.findById(id);
            project.setName(name);
            project.setOwner(owner);
            project.setDescription(description);
            projectDAO.updateProject(project);
            return project;
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void addMetadataCategoryToProject(final Project project, final MetadataCategory metadataCategory)
    {
        final EntityManager em = this.entityManagerFactory.getEntityManager();
        try
        {
            final ProjectDAO projectDAO = this.projectDAOFactory.createInstance(em);
            project.getMetadataCategories().add(metadataCategory);
            projectDAO.updateProject(project);
        }
        finally
        {
            em.close();
        }
    }
}
