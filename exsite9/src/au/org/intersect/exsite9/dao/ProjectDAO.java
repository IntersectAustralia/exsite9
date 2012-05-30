package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.Project;

public class ProjectDAO
{
    private static ProjectDAO instance = null;
    
    private final EntityManager em;
    
    public static synchronized ProjectDAO getInstance(EntityManager em)
    {
        if (instance == null)
        {
            instance = new ProjectDAO(em);
        }
        return instance;
    }

    public static ProjectDAO createTestInstance(EntityManager em)
    {
    	return new ProjectDAO(em);
    }
    
    private ProjectDAO(EntityManager em)
    {
        this.em = em;
    }
    
    public void createProject(Project project)
    {
        em.getTransaction().begin();
        em.persist(project);
        em.getTransaction().commit();
    }
    
    public void updateProject(Project project)
    {
        em.getTransaction().begin();
        em.persist(project);
        em.getTransaction().commit();
    }
    
    public Project findById(long id)
    {
        return em.find(Project.class,id);
    }
}
