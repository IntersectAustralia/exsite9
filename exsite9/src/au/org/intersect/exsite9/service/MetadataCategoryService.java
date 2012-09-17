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

import au.org.intersect.exsite9.dao.MetadataAttributeDAO;
import au.org.intersect.exsite9.dao.MetadataCategoryDAO;
import au.org.intersect.exsite9.dao.factory.MetadataAttributeDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataCategoryDAOFactory;
import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataCategoryViewConfiguration;
import au.org.intersect.exsite9.domain.MetadataValue;

public final class MetadataCategoryService implements IMetadataCategoryService
{
    private final EntityManagerFactory emf;
    private final MetadataCategoryDAOFactory metadataCategoryDAOFactory;
    private final MetadataAttributeDAOFactory metadataAttributeDAOFactory;

    public MetadataCategoryService(final EntityManagerFactory emf, final MetadataCategoryDAOFactory metadataCategoryDAOFactory, final MetadataAttributeDAOFactory metadataAttributeDAOFactory)
    {
        this.emf = emf;
        this.metadataCategoryDAOFactory = metadataCategoryDAOFactory;
        this.metadataAttributeDAOFactory = metadataAttributeDAOFactory;
    }

    /** 
     * @{inheritDoc}
     */
    @Override
    public MetadataCategory createNewMetadataCategory(final String name, final String description, final MetadataCategoryType type, final MetadataCategoryUse use,
            final boolean inextensible, final boolean imported, final List<MetadataValue> values, final MetadataAttribute metadataAttribute)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final MetadataCategoryDAO mdcDAO = this.metadataCategoryDAOFactory.createInstance(em);
            final MetadataAttributeDAO mdaDAO = this.metadataAttributeDAOFactory.createInstance(em);

            final MetadataCategory mdc = new MetadataCategory(name, type, use);
            mdc.setDescription(description);
            mdc.setValues(values);
            mdc.setInextensible(inextensible);
            mdc.setImported(imported);
            
            if (metadataAttribute != null)
            {
                mdaDAO.createMetadataAttribute(metadataAttribute);
                mdc.setMetadataAttribute(metadataAttribute);
            }

            mdcDAO.createMetadataCategory(mdc);
            return mdc;
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
    public void deleteMetadataCategory(final MetadataCategory metadataCategory)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final MetadataCategoryDAO mdcDAO = this.metadataCategoryDAOFactory.createInstance(em);

            final MetadataAttribute mda = metadataCategory.getMetadataAttribute();
            if (mda != null)
            {
                final MetadataAttributeDAO mdaDAO = this.metadataAttributeDAOFactory.createInstance(em);
                metadataCategory.setMetadataAttribute(null);
                mdcDAO.updateMetadataCategory(metadataCategory);
                mdaDAO.delete(mda);
            }

            final MetadataCategoryViewConfiguration mcvc = MetadataCategoryViewConfigService.getMetadataCategoryViewConfiguration(em, metadataCategory);
            if (mcvc != null)
            {
                em.remove(mcvc);
            }

            mdcDAO.delete(metadataCategory);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public MetadataCategory findById(final Long id)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final MetadataCategoryDAO mdcDAO = this.metadataCategoryDAOFactory.createInstance(em);
            return mdcDAO.findById(id);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void updateMetadataCategory(final MetadataCategory existingMetadataCategoryToUpdate, final String name, final String description,
            final MetadataCategoryUse use, final boolean inExtensible, final List<MetadataValue> values, final MetadataAttribute metadataAttribute)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final MetadataCategoryDAO mdcDAO = this.metadataCategoryDAOFactory.createInstance(em);
            final MetadataAttributeDAO mdaDAO = this.metadataAttributeDAOFactory.createInstance(em);

            existingMetadataCategoryToUpdate.setName(name);
            existingMetadataCategoryToUpdate.setDescription(description);
            existingMetadataCategoryToUpdate.setUse(use);
            existingMetadataCategoryToUpdate.setInextensible(inExtensible);
            existingMetadataCategoryToUpdate.setValues(values);

            final MetadataAttribute oldMetadataAttribute = existingMetadataCategoryToUpdate.getMetadataAttribute();
            existingMetadataCategoryToUpdate.setMetadataAttribute(metadataAttribute);
            mdcDAO.updateMetadataCategory(existingMetadataCategoryToUpdate);

            if (metadataAttribute != null)
            {
                mdaDAO.updateMetadataAttribute(metadataAttribute);
            }

            if (oldMetadataAttribute != null && metadataAttribute == null)
            {
                mdaDAO.delete(oldMetadataAttribute);
            }
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public MetadataValue addValueToMetadataCategory(final MetadataCategory metadataCategory, final String metadataValue)
    {
        for (final MetadataValue existingValue : metadataCategory.getValues())
        {
            if (existingValue.getValue().equals(metadataValue))
            {
                return existingValue;
            }
        }

        final MetadataValue newValue = new MetadataValue(metadataValue);
        final List<MetadataValue> existingValues = metadataCategory.getValues();
        existingValues.add(newValue);
        metadataCategory.setValues(existingValues);

        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final MetadataCategoryDAO mdcDAO = this.metadataCategoryDAOFactory.createInstance(em);
            mdcDAO.updateMetadataCategory(metadataCategory);

            final MetadataCategory updatedMetadataCategory = mdcDAO.findById(metadataCategory.getId());
            for (final MetadataValue updatedValue : updatedMetadataCategory.getValues())
            {
                if (updatedValue.getValue().equals(metadataValue))
                {
                    return updatedValue;
                }
            }
            return null;
        }
        finally
        {
            em.close();
        }
    }
}
