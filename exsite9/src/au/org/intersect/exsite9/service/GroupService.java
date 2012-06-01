/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.Group;

/**
 * 
 */
public final class GroupService implements IGroupService
{
    private final ExSite9EntityManagerFactory entityManagerFactory;
    private final GroupDAOFactory groupDAOFactory;

    public GroupService(final ExSite9EntityManagerFactory entityManagerFactory,
                        final GroupDAOFactory groupDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.groupDAOFactory = groupDAOFactory;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Group createNewGroup(final String groupName)
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            final Group group = new Group(groupName);
            groupDAO.createGroup(group);
            return group;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addChildGroup(final Group parentGroup, final Group childGroup)
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            parentGroup.getGroups().add(childGroup);
            groupDAO.updateGroup(parentGroup);
        }
        finally
        {
            em.close();
        }
    }
}
