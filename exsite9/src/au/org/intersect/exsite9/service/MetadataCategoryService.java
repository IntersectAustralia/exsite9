/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.MetadataCategoryDAO;
import au.org.intersect.exsite9.dao.factory.MetadataCategoryDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;

public final class MetadataCategoryService implements IMetadataCategoryService
{
    private final ExSite9EntityManagerFactory emf;
    private final MetadataCategoryDAOFactory metadataCategoryDAOFactory;

    public MetadataCategoryService(final ExSite9EntityManagerFactory emf, final MetadataCategoryDAOFactory metadataCategoryDAOFactory)
    {
        this.emf = emf;
        this.metadataCategoryDAOFactory = metadataCategoryDAOFactory;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public MetadataCategory createNewMetadataCategory(final String name, final List<MetadataValue> values)
    {
        final EntityManager em = this.emf.getEntityManager();
        try
        {
            final MetadataCategoryDAO mdcDAO = this.metadataCategoryDAOFactory.createInstance(em);
            final MetadataCategory mdc = new MetadataCategory(name);
            mdc.setValues(values);
            mdcDAO.createMetadataCategory(mdc);
            return mdc;
        }
        finally
        {
            em.close();
        }
    }
}
