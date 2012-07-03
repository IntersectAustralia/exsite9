package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

public class ProjectServiceUnitTest extends DAOTest
{
    private static final String EMPTY_STRING = "";
    private ProjectService projectService;
    
    @Test
    public void createNewProjectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);

        stub(emf.createEntityManager()).toReturn(createEntityManager());
        
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        
        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, groupDAOFactory, researchFileDAOFactory, metadataAssociationDAOFactory);
        
        Project project = projectService.createProject(new ProjectFieldsDTO("name","owner","description", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING));

        
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
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        
        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, groupDAOFactory, researchFileDAOFactory, metadataAssociationDAOFactory);
        
        Folder folder = new Folder(new File("/tmp"));
        
        Project project = projectService.createProject(new ProjectFieldsDTO("name","owner","description", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING));

        
        projectService.mapFolderToProject(project, folder);
        
        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());
        
        assertEquals(project, newProject);
    }
    
    @Test
    public void removeFoldersFromprojectTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager())
                                       .toReturn(createEntityManager());
        
        Folder folder1 = new Folder(new File("/tmp1"));
        Folder folder2 = new Folder(new File("/tmp2"));
        
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        
        projectService = new ProjectService(emf, projectDAOFactory, folderDAOFactory, groupDAOFactory, researchFileDAOFactory, metadataAssociationDAOFactory);
        
        Project project = projectService.createProject(new ProjectFieldsDTO("name","owner","description", EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING));
        
        projectService.mapFolderToProject(project, folder1);
        projectService.mapFolderToProject(project, folder2);
        
        List<String> modifiedFolderList = new ArrayList<String>(0);
        
        modifiedFolderList.add(folder2.getPath());
        
        projectService.removeFoldersFromProject(project, modifiedFolderList);
        
        Project newProject = projectDAOFactory.createInstance(createEntityManager()).findById(project.getId());
        
        assertEquals(1,newProject.getFolders().size());
        assertEquals(folder2.getPath(),newProject.getFolders().get(0).getPath());
    }
}
