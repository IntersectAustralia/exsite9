package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.JPATest;
import au.org.intersect.exsite9.dao.ProjectDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;

public class FileServiceUnitTest extends JPATest
{

	private final String testDirName = System.getProperty("java.io.tmpdir") + File.separator + "exsite9-FolderUnitTest";
    private File testDirFile = null;
    
    private FileService fileService = null;
    
    private static ProjectDAO projectDAO;
    private static ResearchFileDAO researchFileDAO;
    
    private static EntityManager em;
    
    @BeforeClass
    public static void setupOnce()
    {
        em = createEntityManager();
        projectDAO = ProjectDAO.createTestInstance(em);
        researchFileDAO = ResearchFileDAO.createTestInstance(em);
    }
    
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
    	Project project = new Project("name","owner","description");
    	Folder f = new Folder(testDirFile);
    	
    	project.getFolders().add(f);
    	
    	fileService = new FileService(projectDAO,researchFileDAO);
    	
    	fileService.identifyNewFilesForProject(project);
    	
    	assertTrue(project.getNewFilesNode().getResearchFiles().isEmpty());
    }
}
