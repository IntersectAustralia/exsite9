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

import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.service.ProjectService;

/**
 * Factory responsible for creating {@link ProjectService}
 */
public final class ProjectServiceFactory extends AbstractServiceFactory
{
    
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
        final EntityManagerFactory emf = (EntityManagerFactory) PlatformUI.getWorkbench().getService(EntityManagerFactory.class);
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        return new ProjectService(emf, projectDAOFactory, folderDAOFactory, groupDAOFactory, researchFileDAOFactory,metadataAssociationDAOFactory);
    }
}