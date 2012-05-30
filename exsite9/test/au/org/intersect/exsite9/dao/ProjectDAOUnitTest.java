package au.org.intersect.exsite9.dao;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;

public class ProjectDAOUnitTest extends JPATest
{
    private static ProjectDAO projectDAO = null;
    private static EntityManager em;
    
    @BeforeClass
    public static void setupOnce()
    {
        em = createEntityManager();
        projectDAO = ProjectDAO.getInstance(em);
    }
    
    @Test
    public void constructorTest()
    {
    	ProjectDAO projectDAO2 = ProjectDAO.getInstance(em);
        assertEquals(projectDAO,projectDAO2);
    }
    
    @Test
    public void createNewProjectTest()
    {
        Project project = new Project();
        project.setName("Project One");
        project.setDescription("This is project one.");
        project.setOwner("Owner One");
        
        Group rootNode = new Group(project.getName());
        Group newFilesNode = new Group("New Files");
        
        project.setRootNode(rootNode);
        project.setNewFilesNode(newFilesNode);
        
        projectDAO = ProjectDAO.getInstance(em);
        projectDAO.createProject(project);
        
        Project project2 = projectDAO.findById(project.getId());
        
        assertEquals(project, project2);
    }
}
