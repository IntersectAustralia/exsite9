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

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

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
        em.getTransaction().begin();
        em.persist(file);
        em.getTransaction().commit();
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
        final TypedQuery<ResearchFile> query = em.createQuery("SELECT r FROM ResearchFile r WHERE r.file = :file AND r.projectID = :projectID", ResearchFile.class);
        query.setParameter("file", path);
        query.setParameter("projectID", project.getId());

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
}
