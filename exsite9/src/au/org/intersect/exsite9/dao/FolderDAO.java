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
}
