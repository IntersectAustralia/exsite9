/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Group;

/**
 * 
 */
public final class GroupDAO
{
    private static GroupDAO INSTANCE;

    private final EntityManager em;

    private GroupDAO(final EntityManager entityManager)
    {
        this.em = entityManager;
    }

    public static synchronized GroupDAO getInstance(final EntityManager em)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new GroupDAO(em);
        }
        return INSTANCE;
    }

    public void createGroup(final Group group)
    {
        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();
    }

    public void updateGroup(final Group group)
    {
        em.getTransaction().begin();
        em.merge(group);
        em.getTransaction().commit();
    }
}
