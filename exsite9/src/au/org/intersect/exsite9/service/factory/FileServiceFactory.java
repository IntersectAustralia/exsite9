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

import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.service.FileService;

/**
 * Factory responsible for creating {@link FileService}
 */
public final class FileServiceFactory extends AbstractServiceFactory
{

    public FileServiceFactory()
    {
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Object create(final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator)
    {
        final EntityManager em = ExSite9EntityManagerFactory.createEntityManager();
        final ProjectDAO projectDAO = ProjectDAO.getInstance(em);
        final ResearchFileDAO researchFileDAO = ResearchFileDAO.getInstance(em);
        
        return new FileService(projectDAO,researchFileDAO);
    }
}