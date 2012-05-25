package au.org.intersect.exsite9.manager;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Folder;

public class FolderManager
{

    private final EntityManager em;
    
    public FolderManager(EntityManager em)
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
