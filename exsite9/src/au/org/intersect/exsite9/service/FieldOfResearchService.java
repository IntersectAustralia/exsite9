/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import au.org.intersect.exsite9.dao.FieldOfResearchDAO;
import au.org.intersect.exsite9.dao.factory.FieldOfResearchDAOFactory;
import au.org.intersect.exsite9.domain.FieldOfResearch;

/**
 * uses {@link FieldOfResearchDAO} to access the {@link FieldOfResearch}s in the database
 */
public final class FieldOfResearchService implements IFieldOfResearchService
{
    private final EntityManagerFactory emf;
    private final FieldOfResearchDAOFactory fieldOfResearchDAOFactory;

    public FieldOfResearchService(final EntityManagerFactory entityManagerFactory, final FieldOfResearchDAOFactory fieldOfResearchDAOFactory)
    {
        this.emf = entityManagerFactory;
        this.fieldOfResearchDAOFactory = fieldOfResearchDAOFactory;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public List<FieldOfResearch> getAll()
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final FieldOfResearchDAO fieldOfResearchDAO = fieldOfResearchDAOFactory.createInstance(em);
            return fieldOfResearchDAO.getAll();
        }
        finally
        {
            em.close();
        }
    }
}
