/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service.factory;

import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;

import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
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
        final ExSite9EntityManagerFactory emf = new ExSite9EntityManagerFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        
        return new FileService(emf,projectDAOFactory,researchFileDAOFactory);
    }
}