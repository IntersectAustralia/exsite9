package au.org.intersect.exsite9.service;

import javax.persistence.EntityManager;

import org.junit.Test;

import au.org.intersect.exsite9.dao.JPATest;
import au.org.intersect.exsite9.dao.ProjectDAO;

public class ProjectServiceUnitTest extends JPATest
{

    private ProjectService projectService;
    
    @Test
    public void createNewProjectTest()
    {
        EntityManager em = createEntityManager();
        ProjectDAO dao = new ProjectDAO(em);
        
        projectService = new ProjectService(dao);
        
        projectService.createProject("Project One","This is project one.");
        
        em.close();
    }
    
}
