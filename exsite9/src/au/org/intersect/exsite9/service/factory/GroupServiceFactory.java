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

import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.service.GroupService;

/**
 * Factory responsible for creating {@link GroupService}
 */
public final class GroupServiceFactory extends AbstractServiceFactory
{
    
    public GroupServiceFactory()
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
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        final ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();

        return new GroupService(emf, groupDAOFactory, metadataAssociationDAOFactory, researchFileDAOFactory);
    }
}
