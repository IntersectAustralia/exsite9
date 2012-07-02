/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.MetadataAssociation;

/**
 * 
 */
public final class MetadataAssociationDAO
{
    private EntityManager em;

    public MetadataAssociationDAO(final EntityManager em)
    {
        this.em = em;
    }

    public void createMetadataAssociation(final MetadataAssociation metadataAssociation)
    {
        em.getTransaction().begin();
        em.persist(metadataAssociation);
        em.getTransaction().commit();
    }

    public void updateMetadataAssociation(final MetadataAssociation metadataAssociation)
    {
        em.getTransaction().begin();
        em.merge(metadataAssociation);
        em.getTransaction().commit();
    }

    public void removeMetadataAssociation(final MetadataAssociation metadataAssociation)
    {
        final boolean transactionActive = em.getTransaction().isActive();
        if (!transactionActive){em.getTransaction().begin();}
        em.remove(em.merge(metadataAssociation));
        if (!transactionActive){em.getTransaction().commit();}
    }
}
