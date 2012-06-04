package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.util.Iterator;
import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

public class FileServiceUnitTest extends DAOTest
{
	private final String testDirName = System.getProperty("java.io.tmpdir") + File.separator + "exsite9-FolderUnitTest";
    private File testDirFile = null;
    
    private FileService fileService = null;
    
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
        ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
        stub(emf.getEntityManager()).toReturn(createEntityManager());
        
        ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
        
    	Project project = new Project("name","owner","description");
    	Folder f = new Folder(testDirFile);
    	
    	project.getFolders().add(f);
    	
    	fileService = new FileService(emf, projectDAOFactory,researchFileDAOFactory);
    	
    	fileService.identifyNewFilesForProject(project);
    	
    	assertTrue(project.getNewFilesNode().getResearchFiles().isEmpty());
    }

    
    @Test
    public void identifyNewFilesForProjectSingleFileTest()
    {
    	try
    	{
    	    ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
            stub(emf.getEntityManager()).toReturn(createEntityManager());
            
            ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
            ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
            
	    	Project project = new Project("name","owner","description");
	    	project.setId(1L);
	    	Folder f = new Folder(testDirFile);
	    	File.createTempFile("test-file-1", ".txt", testDirFile);
	    	
	    	project.getFolders().add(f);
	    	
	    	fileService = new FileService(emf, projectDAOFactory,researchFileDAOFactory);
	    	
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
    	catch(Exception e)
    	{
    		fail("Unexpected exception thrown: " + e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    
    @Test
    public void testIdentifyNewFiles()
    {
        try
        {
            ExSite9EntityManagerFactory emf = mock(ExSite9EntityManagerFactory.class);
            stub(emf.getEntityManager()).toReturn(createEntityManager())
                                        .toReturn(createEntityManager())
                                        .toReturn(createEntityManager());
            
            ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
            ResearchFileDAOFactory researchFileDAOFactory = new ResearchFileDAOFactory();
            
        	Project project = new Project("name","owner","description");
        	project.setId(2L);
            Folder f = new Folder(testDirFile);
            project.getFolders().add(f);
            
            fileService = new FileService(emf, projectDAOFactory,researchFileDAOFactory);
            
            File.createTempFile("test-file-1", ".txt", testDirFile);
            
            fileService.identifyNewFilesForProject(project);
            assertEquals("List has one entry",1,project.getNewFilesNode().getResearchFiles().size());
            
            fileService.identifyNewFilesForProject(project);
            assertEquals("List has one entry",1,project.getNewFilesNode().getResearchFiles().size());
            
            File.createTempFile("test-file-2", ".txt", testDirFile);
            
            fileService.identifyNewFilesForProject(project);
            assertEquals("List has two entries",2,project.getNewFilesNode().getResearchFiles().size());
        }
        catch(Exception e)
        {
            fail("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
