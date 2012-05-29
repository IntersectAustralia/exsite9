/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service.factory;

import javax.persistence.EntityManager;

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
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        final EntityManager em = ExSite9EntityManagerFactory.createEntityManager();
        final ProjectDAO projectDAO = ProjectDAO.getInstance(em);
        final FolderDAO folderDAO = FolderDAO.getInstance(em);
        
        return new ProjectService(projectDAO, folderDAO);
    }
}