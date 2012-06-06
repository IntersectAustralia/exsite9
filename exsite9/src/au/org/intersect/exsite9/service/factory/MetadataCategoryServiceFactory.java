/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import au.org.intersect.exsite9.dao.factory.MetadataCategoryDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.service.MetadataCategoryService;

/**
 * 
 */
public final class MetadataCategoryServiceFactory extends AbstractServiceFactory
{

    /**
     * @{inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object create(Class serviceInterface, IServiceLocator parentLocator, IServiceLocator locator)
    {
        final ExSite9EntityManagerFactory entityManagerFactory = new ExSite9EntityManagerFactory();
        final MetadataCategoryDAOFactory mdcDAOFactory = new MetadataCategoryDAOFactory();
        return new MetadataCategoryService(entityManagerFactory, mdcDAOFactory);
    }

}
