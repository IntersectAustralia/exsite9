package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.Test;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.JPATest;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;

public class ProjectServiceUnitTest extends JPATest
{

    private ProjectService projectService;
    
    @Test
    public void createNewProjectTest()
    {
        EntityManager em = createEntityManager();
        
        ProjectDAO projectDAO = new ProjectDAO(em);
        FolderDAO folderDAO = new FolderDAO(em);
        
        projectService = new ProjectService(projectDAO, folderDAO);
        
        Project project = projectService.createProject("Project One","This is project one.");
        
        Project newProject = projectDAO.findById(project.getId());
        
        assertEquals(project, newProject);
        
        em.close();
    }
    
    @Test
    public void mapFolderToProjectTest()
    {
        EntityManager em = createEntityManager();
        
        ProjectDAO projectDAO = new ProjectDAO(em);
        FolderDAO folderDAO = new FolderDAO(em);
        
        projectService = new ProjectService(projectDAO, folderDAO);
        
        Project project = projectService.createProject("Project One","This is project one.");
        
        Folder folder = new Folder(new File("/tmp"));
        
        projectService.mapFolderToProject(project, folder);
        
        Project newProject = projectDAO.findById(project.getId());
        
        assertEquals(project, newProject);
        
        em.close();
    }
    
    
}
