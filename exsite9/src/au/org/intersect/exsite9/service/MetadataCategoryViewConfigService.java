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
import javax.persistence.TypedQuery;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryViewConfiguration;

/**
 * Metadata Category view configuration service.
 */
public final class MetadataCategoryViewConfigService implements IMetadataCategoryViewConfigService
{
    private final EntityManagerFactory entityManagerFactory;

    public MetadataCategoryViewConfigService(final EntityManagerFactory emf)
    {
        this.entityManagerFactory = emf;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isExpanded(final MetadataCategory metadataCategory)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final MetadataCategoryViewConfiguration mcvc = getMetadataCategoryViewConfiguration(em, metadataCategory);
            if (mcvc != null)
            {
                return mcvc.getExpanded();
            }
            return false;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void setExpanded(final MetadataCategory metadataCategory, final boolean expanded)
    {
        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            em.getTransaction().begin();
            final MetadataCategoryViewConfiguration existing = getMetadataCategoryViewConfiguration(em, metadataCategory);
            if (existing == null)
            {
                final MetadataCategoryViewConfiguration newMcvc = new MetadataCategoryViewConfiguration(metadataCategory, Boolean.valueOf(expanded));
                em.persist(newMcvc);
            }
            else
            {
                existing.setExpanded(Boolean.valueOf(expanded));
                em.merge(existing);
            }
        }
        finally
        {
            em.getTransaction().commit();
            em.close();
        }
    }

    static MetadataCategoryViewConfiguration getMetadataCategoryViewConfiguration(final EntityManager em, final MetadataCategory metadataCategory)
    {
        final String queryJQL = "SELECT m FROM MetadataCategoryViewConfiguration m WHERE m.metadataCategory = :category";
        final TypedQuery<MetadataCategoryViewConfiguration> query = em.createQuery(queryJQL, MetadataCategoryViewConfiguration.class);
        query.setParameter("category", metadataCategory);
        final List<MetadataCategoryViewConfiguration> viewConfigs = query.getResultList();
        if (viewConfigs.isEmpty())
        {
            return null;
        }

        return viewConfigs.get(0);
    }
}
