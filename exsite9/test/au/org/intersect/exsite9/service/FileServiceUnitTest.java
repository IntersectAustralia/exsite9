package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Iterator;
import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.intersect.exsite9.dao.JPATest;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

public class FileServiceUnitTest extends JPATest
{

	private final String testDirName = System.getProperty("java.io.tmpdir") + File.separator + "exsite9-FolderUnitTest";
    private File testDirFile = null;
    
    private FileService fileService = null;
    
    private static ProjectDAO projectDAO;
    private static ResearchFileDAO researchFileDAO;
    
    private static EntityManager em;
    
    @Before
    public void setUp()
    {
    	em = createEntityManager();
        projectDAO = ProjectDAO.createTestInstance(em);
        researchFileDAO = ResearchFileDAO.createTestInstance(em);
        
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
        
        em.close();
    }
    
    @Test
    public void identifyNewFilesForProjectEmptyFolderTest()
    {
    	Project project = new Project("name","owner","description");
    	Folder f = new Folder(testDirFile);
    	
    	project.getFolders().add(f);
    	
    	fileService = new FileService(projectDAO,researchFileDAO);
    	
    	fileService.identifyNewFilesForProject(project);
    	
    	assertTrue(project.getNewFilesNode().getResearchFiles().isEmpty());
    }

    
    @Test
    public void identifyNewFilesForProjectSingleFileTest()
    {
    	try
    	{
	    	Project project = new Project("name","owner","description");
	    	Folder f = new Folder(testDirFile);
	    	File.createTempFile("test-file-1", ".txt", testDirFile);
	    	
	    	project.getFolders().add(f);
	    	
	    	fileService = new FileService(projectDAO,researchFileDAO);
	    	
	    	fileService.identifyNewFilesForProject(project);
	    	
	    	assertTrue(project.getNewFilesNode().getResearchFiles().size() == 1);
	    	
	    	Iterator<ResearchFile> iter = project.getNewFilesNode().getResearchFiles().iterator();
	    	
	    	while(iter.hasNext())
	    	{
	    		ResearchFile file1 = iter.next();
	    		ResearchFile file2 = researchFileDAO.findById(file1.getId());
	    		assertEquals(file1, file2);
	    	}
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
        	Project project = new Project("name","owner","description");
            Folder f = new Folder(testDirFile);
            project.getFolders().add(f);
            
            fileService = new FileService(projectDAO,researchFileDAO);
            
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
