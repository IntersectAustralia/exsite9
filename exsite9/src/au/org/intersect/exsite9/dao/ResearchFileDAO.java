/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.dao;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Data Access Object for {@link ResearchFile} instances
 */
public class ResearchFileDAO
{
    private static final Logger LOG = Logger.getLogger(ResearchFileDAO.class);

    private EntityManager em;
    
    public ResearchFileDAO(EntityManager em)
    {
        this.em = em;
    }
    
    public void createResearchFile(ResearchFile file)
    {
        final boolean transactionActive = em.getTransaction().isActive();
        if (!transactionActive){em.getTransaction().begin();}
        em.persist(file);
        if (!transactionActive){ em.getTransaction().commit(); }
    }
    
    public void removeResearchFile(final ResearchFile file)
    {
        final boolean transactionActive = em.getTransaction().isActive();
        if (!transactionActive)
        {
            em.getTransaction().begin();
        }
        em.remove(em.merge(file));
        if (!transactionActive)
        {
            em.getTransaction().commit();
        }
    }
    
    public ResearchFile findById(long id)
    {
        return em.find(ResearchFile.class,id);
    }

    /**
     * Finds a ResearchFile by its path.
     * @param project The project the ResearchFile is for.
     * @param path The full path of the ResearchFile.
     * @return The research file, or {@code null} if it does not exist.
     */
    public ResearchFile findByPath(final Project project, final File path)
    {
        final TypedQuery<ResearchFile> query = em.createQuery("SELECT r FROM ResearchFile r WHERE r.file = :file AND r.project = :project", ResearchFile.class);
        query.setParameter("file", path);
        query.setParameter("project", project);

        final List<ResearchFile> matches = query.getResultList();
        if (matches.size() > 1)
        {
            LOG.error("Multiple files with the same path defined in the project");
            return null;
        }

        if (matches.isEmpty())
        {
            return null;
        }
        return matches.get(0);
    }

    public ResearchFile updateResearchFile(final ResearchFile researchFile)
    {
        boolean localTransaction = (em.getTransaction().isActive()) ? false : true;
        
        if (localTransaction)
        {
            em.getTransaction().begin();
        }
        final ResearchFile updatedResearchFile = em.merge(researchFile);
        if(localTransaction)
        {
            em.getTransaction().commit();
        }
        return updatedResearchFile;
    }
    
    public Folder getParentFolder(final ResearchFile researchFile)
    {
        final TypedQuery<Folder> query = em.createQuery("SELECT f FROM Folder f WHERE :researchFile MEMBER OF f.files", Folder.class);
        query.setParameter("researchFile", researchFile);
        return query.getSingleResult();
    }
}
