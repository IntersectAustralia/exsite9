package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.JPATest;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;

public class ProjectServiceUnitTest extends JPATest
{

    private static EntityManager em;
    private static ProjectDAO projectDAO;
    private static FolderDAO folderDAO;
    
    private ProjectService projectService;
    
    @BeforeClass
    public static void setupOnce()
    {
        em = createEntityManager();
        projectDAO = ProjectDAO.getInstance(em);
        folderDAO = new FolderDAO(em);
    }
    
    @Test
    public void createNewProjectTest()
    {
        projectService = new ProjectService(projectDAO, folderDAO);
        
        Project project = projectService.createProject("Project One","Ownwer One","This is project one.");
        
        Project newProject = projectDAO.findById(project.getId());
        
        assertEquals(project, newProject);
    }
    
    @Test
    public void mapFolderToProjectTest()
    {
        projectService = new ProjectService(projectDAO, folderDAO);
        
        Project project = projectService.createProject("Project One","Ownwer One","This is project one.");
        
        Folder folder = new Folder(new File("/tmp"));
        
        projectService.mapFolderToProject(project, folder);
        
        Project newProject = projectDAO.findById(project.getId());
        
        assertEquals(project, newProject);
    }
    
    
}
