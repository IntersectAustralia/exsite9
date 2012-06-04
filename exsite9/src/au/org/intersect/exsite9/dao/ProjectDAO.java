package au.org.intersect.exsite9.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import au.org.intersect.exsite9.domain.Project;

public class ProjectDAO
{
    private final EntityManager em;
    
    public ProjectDAO(EntityManager em)
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
        em.merge(project);
        em.getTransaction().commit();
    }
    
    public Project findById(long id)
    {
        return em.find(Project.class,id);
    }
    
    public List<Project> findAllProjects()
    {
        Query query = em.createQuery("SELECT p FROM Project p");
        return query.getResultList();
    }
}
