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

import au.org.intersect.exsite9.service.SearchService;

/**
 * Factory responsible for creating {@link SearchService}
 */
public final class SearchServiceFactory extends AbstractServiceFactory
{

    @Override
    @SuppressWarnings("rawtypes")
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        final EntityManagerFactory emf = (EntityManagerFactory) PlatformUI.getWorkbench().getService(EntityManagerFactory.class);
        return new SearchService(emf);
    }

}
