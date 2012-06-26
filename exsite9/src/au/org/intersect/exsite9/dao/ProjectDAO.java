package au.org.intersect.exsite9.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
    
    public Project updateProject(Project project)
    {
        boolean localTransaction = (em.getTransaction().isActive()) ? false : true;
        if (localTransaction) {em.getTransaction().begin();}
        Project updatedProject = em.merge(project);
        if (localTransaction) { em.getTransaction().commit(); }
        return updatedProject;
    }
    
    public Project findById(long id)
    {
        return em.find(Project.class,id);
    }
    
    public List<Project> findAllProjects()
    {
        TypedQuery<Project> query = em.createQuery("SELECT p FROM Project p", Project.class);
        return query.getResultList();
    }
}
