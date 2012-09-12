/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.MetadataAttribute;

/**
 * Handles Persistence of {@link MetadataAttribute}
 */
public final class MetadataAttributeDAO
{
    private final EntityManager em;

    public MetadataAttributeDAO(final EntityManager em)
    {
        this.em = em;
    }

    public void createMetadataAttribute(final MetadataAttribute mda)
    {
        em.getTransaction().begin();
        em.persist(mda);
        em.getTransaction().commit();
    }

    public void updateMetadataAttribute(final MetadataAttribute mda)
    {
        em.getTransaction().begin();
        em.merge(mda);
        em.getTransaction().commit();
    }

    public MetadataAttribute findById(final Long id)
    {
        return em.find(MetadataAttribute.class, id);        
    }

    public void delete(final MetadataAttribute mda)
    {
        em.getTransaction().begin();
        em.remove(em.merge(mda));
        em.getTransaction().commit();
    }
}
