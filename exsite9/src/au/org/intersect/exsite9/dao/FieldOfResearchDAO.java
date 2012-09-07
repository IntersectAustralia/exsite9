/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import au.org.intersect.exsite9.domain.FieldOfResearch;

/**
 * Data Access Object for {@link FieldOfResearch} instances
 */
public final class FieldOfResearchDAO
{
    private final EntityManager em;

    public FieldOfResearchDAO(final EntityManager em)
    {
        this.em = em;
    }

    /**
     * Obtains all the {@link FieldOfResearch} instances.
     * @return All the fields of research.
     */
    public List<FieldOfResearch> getAll()
    {
        final TypedQuery<FieldOfResearch> query = em.createQuery("SELECT for FROM FieldOfResearch for ORDER BY for.code", FieldOfResearch.class);
        return query.getResultList();
    }
}
