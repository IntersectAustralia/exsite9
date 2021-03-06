/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.MetadataCategory;

/**
 * Handles persistence of {@link MetadataCategory}.
 */
public final class MetadataCategoryDAO
{
    private final EntityManager em;

    public MetadataCategoryDAO(final EntityManager em)
    {
        this.em = em;
    }

    public void createMetadataCategory(final MetadataCategory mdc)
    {
        em.getTransaction().begin();
        em.persist(mdc);
        em.getTransaction().commit();
    }

    public void updateMetadataCategory(final MetadataCategory mdc)
    {
        em.getTransaction().begin();
        em.merge(mdc);
        em.getTransaction().commit();
    }

    public MetadataCategory findById(final Long id)
    {
        return em.find(MetadataCategory.class,id);        
    }

    public void delete(final MetadataCategory mdc)
    {
        em.getTransaction().begin();
        em.remove(em.merge(mdc));
        em.getTransaction().commit();
    }
}
