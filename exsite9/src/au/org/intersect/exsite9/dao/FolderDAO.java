/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Folder;

/**
 * Data Access Object for {@link Folder} instances
 */
public class FolderDAO
{
    private EntityManager em;
    
    public FolderDAO(final EntityManager em)
    {
        this.em = em;
    }
    
    public void createFolder(final Folder folder)
    {
        final boolean transactionActive = em.getTransaction().isActive();
        if (!transactionActive)
        {
            em.getTransaction().begin();
        }
        em.persist(folder);
        if (!transactionActive)
        {
            em.getTransaction().commit();
        }
    }
    
    public Folder updateFolder(final Folder folder)
    {
        final boolean transactionActive = em.getTransaction().isActive();
        if (!transactionActive)
        {
            em.getTransaction().begin();
        }
        final Folder updatedFolder = em.merge(folder);
        if (!transactionActive)
        {
            em.getTransaction().commit();
        }
        return updatedFolder;
    }
    
    public void removeFolder(final Folder folder)
    {
        final boolean transactionActive = em.getTransaction().isActive();
        if (!transactionActive)
        {
            em.getTransaction().begin();
        }
        em.remove(em.merge(folder));
        if (!transactionActive)
        {
            em.getTransaction().commit();
        }
    }
    
    public Folder findById(final long id)
    {
        return em.find(Folder.class, id);
    }
}
