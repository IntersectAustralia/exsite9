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

import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.service.GroupService;

/**
 * 
 */
public final class GroupServiceFactory extends AbstractServiceFactory
{

    /**
     * 
     */
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
        final EntityManager em = ExSite9EntityManagerFactory.createEntityManager();
        final GroupDAO groupDAO = GroupDAO.getInstance(em);
        return new GroupService(groupDAO);
    }
}
