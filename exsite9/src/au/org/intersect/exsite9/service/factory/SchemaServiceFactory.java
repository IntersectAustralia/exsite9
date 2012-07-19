/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service.factory;

import javax.persistence.EntityManagerFactory;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import au.org.intersect.exsite9.dao.factory.SchemaDAOFactory;
import au.org.intersect.exsite9.service.SchemaService;

/**
 * Responsible for creating instances of {@link SchemaService}
 */
public final class SchemaServiceFactory extends AbstractServiceFactory
{

    /**
     * 
     */
    public SchemaServiceFactory()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        final EntityManagerFactory emf = (EntityManagerFactory) PlatformUI.getWorkbench().getService(EntityManagerFactory.class);
        final SchemaDAOFactory schemaDAOFactory = new SchemaDAOFactory();
        final SchemaService schemaService = new SchemaService(emf, schemaDAOFactory);
        return schemaService;
    }
}
