/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link ResearchFileService}
 */
public class FileServiceUnitTest extends DAOTest
{
	private final String testDirName = System.getProperty("java.io.tmpdir") + File.separator + "exsite9-FolderUnitTest";
    private File testDirFile = null;
    
    private ResearchFileService fileService = null;
    
    @Before
    public void setUp()
    {
        testDirFile = new File(testDirName);
        testDirFile.mkdir();
    }
    
    @After
    public void tearDown()
    {
        File testDirFile = new File(testDirName);
        
        File[] files = testDirFile.listFiles();
        for(File file : files)
        {
            file.delete();
        }
        testDirFile.delete();
    }
    
    @Test
    public void identifyNewFilesForProjectEmptyFolderTest()
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager());
        
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        ProjectDAO projectDAO = projectDAOFactory.createInstance(emf.createEntityManager());
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        final MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
      
    	Project project = new Project(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"));
    	projectDAO.createProject(project);

    	Folder f = new Folder(testDirFile);
    	
    	project.getFolders().add(f);

    	fileService = new ResearchFileService(emf, projectDAOFactory,researchFileDAOFactory, metadataAssociationDAOFactory,folderDAOFactory, groupDAOFactory);
    	
    	fileService.identifyNewFilesForProject(project);
    	
    	assertTrue(project.getNewFilesNode().getResearchFiles().isEmpty());
    }

    
    @Test
    public void identifyNewFilesForProjectSingleFileTest() throws IOException
    {
	    EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager());
        
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        
    	Project project = new Project(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"));
    	Folder f = new Folder(testDirFile);
    	File.createTempFile("test-file-1", ".txt", testDirFile);

    	project.getFolders().add(f);

    	ProjectDAO projectDAO = new ProjectDAO(createEntityManager());
    	projectDAO.createProject(project);

    	fileService = new ResearchFileService(emf, projectDAOFactory,researchFileDAOFactory, metadataAssociationDAOFactory,folderDAOFactory, groupDAOFactory);
    	
    	fileService.identifyNewFilesForProject(project);
    	
    	assertTrue(project.getNewFilesNode().getResearchFiles().size() == 1);
    	
    	EntityManager em = createEntityManager();
    	ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
    	Iterator<ResearchFile> iter = project.getNewFilesNode().getResearchFiles().iterator();
    	
    	while(iter.hasNext())
    	{
    		ResearchFile file1 = iter.next();
    		ResearchFile file2 = researchFileDAO.findById(file1.getId());
    		assertEquals(file1, file2);
    	}
    	
    	em.close();
    }
    
    
    @Test
    public void testIdentifyNewFiles() throws IOException
    {
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager())
                                    .toReturn(createEntityManager())
                                    .toReturn(createEntityManager());
        
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        MetadataAssociationDAOFactory metadataAssociationDAOFactory = new MetadataAssociationDAOFactory();
        FolderDAOFactory folderDAOFactory = new FolderDAOFactory();
        GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        
    	Project project = new Project(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"));
    	project.setId(2L);

    	final ProjectDAO projectDAO = new ProjectDAO(createEntityManager());
    	projectDAO.createProject(project);

        Folder f = new Folder(testDirFile);
        project.getFolders().add(f);
        
        fileService = new ResearchFileService(emf, projectDAOFactory,researchFileDAOFactory, metadataAssociationDAOFactory,folderDAOFactory, groupDAOFactory);
        
        File.createTempFile("test-file-1", ".txt", testDirFile);
        
        fileService.identifyNewFilesForProject(project);
        assertEquals("List has one entry",1,project.getNewFilesNode().getResearchFiles().size());
        
        fileService.identifyNewFilesForProject(project);
        assertEquals("List has one entry",1,project.getNewFilesNode().getResearchFiles().size());
        
        File.createTempFile("test-file-2", ".txt", testDirFile);
        
        fileService.identifyNewFilesForProject(project);
        assertEquals("List has two entries",2,project.getNewFilesNode().getResearchFiles().size());
    }
    
}
