/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Group;

public final class GroupDAO
{
    private EntityManager em;

    public GroupDAO(final EntityManager entityManager)
    {
        this.em = entityManager;
    }

    public void createGroup(final Group group)
    {
        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();
    }

    public void updateGroup(final Group group)
    {
        boolean localTransaction = (em.getTransaction().isActive()) ? false : true;
        
        if (localTransaction)
        {
            em.getTransaction().begin();
        }
        em.merge(group);
        if(localTransaction)
        {
            em.getTransaction().commit();
        }
    }
    
    public Group findById(final long id)
    {
        return em.find(Group.class,id);
    }
}
