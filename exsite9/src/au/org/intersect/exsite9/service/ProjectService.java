/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

public class ProjectService implements IProjectService
{
    private final Logger LOG = Logger.getLogger(ProjectService.class);
    
    private final EntityManagerFactory entityManagerFactory;
    private final ProjectDAOFactory projectDAOFactory;
    private final FolderDAOFactory folderDAOFactory;
    private final GroupDAOFactory groupDAOFactory;
    private final ResearchFileDAOFactory researchFileDAOFactory;

    public ProjectService(final EntityManagerFactory entityManagerFactory,
            final ProjectDAOFactory projectDAOFactory, final FolderDAOFactory folderDAOFactory, 
            final GroupDAOFactory groupDAOFactory, final ResearchFileDAOFactory researchFileDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.projectDAOFactory = projectDAOFactory;
        this.folderDAOFactory = folderDAOFactory;
        this.groupDAOFactory = groupDAOFactory;
        this.researchFileDAOFactory = researchFileDAOFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project createProject(final String name, final String owner, final String description)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
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
        EntityManager em = entityManagerFactory.createEntityManager();
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
        EntityManager em = entityManagerFactory.createEntityManager();
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
        EntityManager em = entityManagerFactory.createEntityManager();
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
        final EntityManager em = this.entityManagerFactory.createEntityManager();
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

    @Override
    public Project findProjectById(Long id)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final ProjectDAO projectDAO = this.projectDAOFactory.createInstance(em);
            return projectDAO.findById(id);            
        }
        finally
        {
            em.close();
        }        
    }

    @Override
    public Project removeFoldersFromProject(Project project, List<String> modifiedFolderList)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final ProjectDAO projectDAO = this.projectDAOFactory.createInstance(em);
            final FolderDAO folderDAO = this.folderDAOFactory.createInstance(em);
            final GroupDAO groupDAO = this.groupDAOFactory.createInstance(em);
            final ResearchFileDAO researchFileDAO = this.researchFileDAOFactory.createInstance(em);
            
            project = projectDAO.findById(project.getId());
            
            Iterator<Folder> folderIter = project.getFolders().iterator();
            while(folderIter.hasNext())
            {
                Folder folder = folderIter.next();
                if(modifiedFolderList.contains(folder.getPath()))
                {
                    continue;
                }
                else
                {
                    LOG.info("Removing folder id= " + folder.getId());
                    
                    em.getTransaction().begin();
                    
                    Iterator<ResearchFile> fileIter = folder.getFiles().iterator();
                    while (fileIter.hasNext()){
                        ResearchFile researchFile = fileIter.next();
                        Group parentGroup = groupDAO.getParent(researchFile);
                        parentGroup.getResearchFiles().remove(researchFile);
                        fileIter.remove();
                        researchFileDAO.removeResearchFile(researchFile);
                    }
                    folderIter.remove();
                    folderDAO.removeFolder(folder);
                    em.getTransaction().commit();
                }
            }
            return project;
        }
        finally
        {
            em.close();
        }        
    }

}
