/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.service.ProjectService;

/**
 * Factory responsible for creating {@link ProjectService}
 */
public final class ProjectServiceFactory extends AbstractServiceFactory
{

    /**
     * 
     */
    public ProjectServiceFactory()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    @SuppressWarnings("rawtypes")
    public Object create(Class serviceInterface, IServiceLocator parentLocator, IServiceLocator locator)
    {
        // TODO: construction of these DAOs should use factories.
        final ProjectDAO projectDAO = new ProjectDAO(ExSite9EntityManagerFactory.createEntityManager());
        final FolderDAO folderDAO = new FolderDAO(ExSite9EntityManagerFactory.createEntityManager());

        final ProjectService projectService = new ProjectService(projectDAO, folderDAO);
        return projectService;
    }
}