package au.org.intersect.exsite9.dao;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;

public class ProjectDAOUnitTest extends DAOTest
{
    private static ProjectDAOFactory projectDAOFactory;
    
    @BeforeClass
    public static void setupOnce()
    {
        projectDAOFactory = new ProjectDAOFactory();
    }
    
    @Test
    public void createNewProjectTest()
    {
        EntityManager em = createEntityManager();
        ProjectDAO projectDAO = projectDAOFactory.createInstance(em);
        
        Project project = new Project();
        project.setName("Project One");
        project.setDescription("This is project one.");
        project.setOwner("Owner One");
        
        Group rootNode = new Group(project.getName());
        Group newFilesNode = new Group("New Files");
        
        project.setRootNode(rootNode);
        project.setNewFilesNode(newFilesNode);
        
        projectDAO.createProject(project);
        
        Project project2 = projectDAO.findById(project.getId());
        
        assertEquals(project, project2);
        
        em.close();
    }
}
