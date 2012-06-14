/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.helper.FolderHelper;

public class FolderHelperUnitTest
{
    
    private final String testDirName = System.getProperty("java.io.tmpdir") + File.separator + "exsite9-FolderUnitTest";
    private File testDirFile = null;
    
    @Before
    public void oneTimeSetUp()
    {
        testDirFile = new File(testDirName);
        testDirFile.mkdir();
    }
    
    @After
    public void oneTimeTearDown()
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
    public void constructorTest()
    {
    	FolderHelper fh = new FolderHelper();
    	assertTrue(fh instanceof FolderHelper);
    }
    
    @Test
    public void testIdentifyNewFilesInvalidFolder()
    {
        String folderName = testDirName + File.separator + "DoesNotExist";
        Folder f = new Folder(new File(folderName));
        List<File> newFiles = FolderHelper.identifyNewFiles(f);
        assertTrue("List is empty",newFiles.isEmpty());
    }
    
    @Test
    public void testIdentifyNewFilesEmptyFolder()
    {
        Folder f = new Folder(testDirFile);
        List<File> newFiles = FolderHelper.identifyNewFiles(f);
        assertTrue("List is empty",newFiles.isEmpty());
    }
    
    @Test
    public void testIdentifyNewFilesSingleFile()
    {
        try
        {
            Folder f = new Folder(testDirFile);
            File.createTempFile("test-file-1", ".txt", testDirFile);
            
            List<File> newFiles = FolderHelper.identifyNewFiles(f);
            assertEquals("List has 1 entry",1,newFiles.size());
        }
        catch(Exception e)
        {
            fail("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIdentifyNewFiles()
    {
        try
        {
            Folder f = new Folder(testDirFile);
            File.createTempFile("test-file-1", ".txt", testDirFile);
            
            try{Thread.sleep(1000);}catch(Exception e){}
            
            List<File> newFiles = FolderHelper.identifyNewFiles(f);
            assertEquals("List has one entry",1,newFiles.size());
            
            try{Thread.sleep(1000);}catch(Exception e){}
            
            newFiles = FolderHelper.identifyNewFiles(f);
            assertTrue("List is empty",newFiles.isEmpty());
            
            File.createTempFile("test-file-2", ".txt", testDirFile);
            
            try{Thread.sleep(1000);}catch(Exception e){}
            
            newFiles = FolderHelper.identifyNewFiles(f);
            assertEquals("List has one entry",1,newFiles.size());
        }
        catch(Exception e)
        {
            fail("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
