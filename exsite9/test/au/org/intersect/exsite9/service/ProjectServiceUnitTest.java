package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;

public class ProjectServiceUnitTest extends DAOTest
{
    private ProjectService projectService;
    
    @Test
    public void createNewProjectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager());
        
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        
        projectService = new ProjectService(emf, projectDAOFactory, new FolderDAOFactory());
        
        Project project = projectService.createProject("Project One","Owner One","This is project one.");
        
        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());
        
        assertEquals(project, newProject);
    }
    
    @Test
    public void mapFolderToProjectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager());

        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        
        Folder folder = new Folder(new File("/tmp"));
        
        projectService = new ProjectService(emf, projectDAOFactory, new FolderDAOFactory());
        
        Project project = projectService.createProject("Project One","Owner One","This is project one.");
        
        projectService.mapFolderToProject(project, folder);
        
        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());
        
        assertEquals(project, newProject);
    }
}
