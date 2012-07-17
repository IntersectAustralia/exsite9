/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.util.FolderUtils;

/**
 * Tests {@link FolderUtils}
 */
public final class FolderUtilsUnitTest
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
    	FolderUtils fh = new FolderUtils();
    	assertTrue(fh instanceof FolderUtils);
    }
    
    @Test
    public void testIdentifyNewFilesEmptyFolder()
    {
        Folder f = new Folder(testDirFile);
        List<File> newFiles = FolderUtils.identifyNewFiles(f);
        assertTrue("List is empty",newFiles.isEmpty());
    }
    
    @Test
    public void testIdentifyNewFilesSingleFile()
    {
        try
        {
            Folder f = new Folder(testDirFile);
            File.createTempFile("test-file-1", ".txt", testDirFile);
            
            List<File> newFiles = FolderUtils.identifyNewFiles(f);
            assertEquals("List has 1 entry",1,newFiles.size());
        }
        catch(Exception e)
        {
            fail("Unexpected exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void testIdentifyNewFiles() throws InterruptedException, IOException
    {
        Folder f = new Folder(testDirFile);
        File.createTempFile("test-file-1", ".txt", testDirFile);
        
        Thread.sleep(1000);
        
        List<File> newFiles = FolderUtils.identifyNewFiles(f);
        assertEquals("List has one entry",1,newFiles.size());
        
        Thread.sleep(1000);
        
        newFiles = FolderUtils.identifyNewFiles(f);
        assertTrue("List is empty",newFiles.isEmpty());
        
        Thread.sleep(1100);

        File.createTempFile("test-file-2", ".txt", testDirFile);
        
        newFiles = FolderUtils.identifyNewFiles(f);
        assertEquals("List has one entry",1,newFiles.size());
    }
}
