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

import au.org.intersect.exsite9.dao.factory.FieldOfResearchDAOFactory;
import au.org.intersect.exsite9.service.FieldOfResearchService;

/**
 * Factory responsible for creating {@link FieldOfResearchService}
 */
public class FieldOfResearchServiceFactory extends AbstractServiceFactory
{

    public FieldOfResearchServiceFactory()
    {
    }

    /**
     * @{inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        final EntityManagerFactory emf = (EntityManagerFactory) PlatformUI.getWorkbench().getService(EntityManagerFactory.class);
        final FieldOfResearchDAOFactory fieldOfResearchDAOFactory = new FieldOfResearchDAOFactory();
        return new FieldOfResearchService(emf, fieldOfResearchDAOFactory);
    }

}
