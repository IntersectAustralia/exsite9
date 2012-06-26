package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Folder;

public class FolderDAO
{
    private EntityManager em;
    
    public FolderDAO(EntityManager em)
    {
        this.em = em;
    }
    
    public void createFolder(Folder folder)
    {
        em.getTransaction().begin();
        em.persist(folder);
        em.getTransaction().commit();
    }
    
    public Folder updateFolder(Folder folder)
    {
        boolean localTransaction = (em.getTransaction().isActive()) ? false : true;
        if (localTransaction) {em.getTransaction().begin();}
        Folder updatedFolder = em.merge(folder);
        if(localTransaction){em.getTransaction().commit();}
        return updatedFolder;
    }
    
    public void removeFolder(final Folder folder)
    {
        boolean localTransaction = (em.getTransaction().isActive()) ? false : true;
        if (localTransaction) {em.getTransaction().begin();}
        em.remove(em.merge(folder));
        if(localTransaction){em.getTransaction().commit();}
    }
}
