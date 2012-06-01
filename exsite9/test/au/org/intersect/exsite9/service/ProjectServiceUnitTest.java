package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;
import java.io.File;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;

public class ProjectServiceUnitTest extends DAOTest
{
    private ProjectService projectService;
    
    @Test
    public void createNewProjectTest()
    {
        ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);

        stub(emf.getEntityManager()).toReturn(createEntityManager());
        
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        
        projectService = new ProjectService(emf, projectDAOFactory, new FolderDAOFactory());
        
        Project project = projectService.createProject("Project One","Owner One","This is project one.");
        
        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());
        
        assertEquals(project, newProject);
    }
    
    @Test
    public void mapFolderToProjectTest()
    {
        ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
        
        stub(emf.getEntityManager()).toReturn(createEntityManager())
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
