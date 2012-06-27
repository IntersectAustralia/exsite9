package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Folder;

public class FolderDAO
{
    private EntityManager em;
    
    public FolderDAO(final EntityManager em)
    {
        this.em = em;
    }
    
    public void createFolder(final Folder folder)
    {
        em.getTransaction().begin();
        em.persist(folder);
        em.getTransaction().commit();
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
}
