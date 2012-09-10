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

    public void createMetadataAttribute(final MetadataAttribute mdc)
    {
        em.getTransaction().begin();
        em.persist(mdc);
        em.getTransaction().commit();
    }

    public void updateMetadataAttribute(final MetadataAttribute mdc)
    {
        em.getTransaction().begin();
        em.merge(mdc);
        em.getTransaction().commit();
    }

    public MetadataAttribute findById(final Long id)
    {
        return em.find(MetadataAttribute.class, id);        
    }

    public void delete(final MetadataAttribute mdc)
    {
        em.getTransaction().begin();
        em.remove(em.merge(mdc));
        em.getTransaction().commit();
    }
}
