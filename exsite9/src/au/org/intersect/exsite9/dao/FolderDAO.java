package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Folder;

public class FolderDAO
{

	private static FolderDAO instance = null;
    private final EntityManager em;
    
    public static FolderDAO getInstance(EntityManager em)
    {
    	if (instance == null)
    	{
    		instance = new FolderDAO(em);
    	}
    	return instance;
    }
    
    private FolderDAO(EntityManager em)
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