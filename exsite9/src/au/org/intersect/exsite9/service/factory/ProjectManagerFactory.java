/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import au.org.intersect.exsite9.service.ProjectManager;

/**
 * Factory responsible for creating {@link ProjectManager}
 */
public final class ProjectManagerFactory extends AbstractServiceFactory
{
    /**
     * @{inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        return new ProjectManager();
    }
}
