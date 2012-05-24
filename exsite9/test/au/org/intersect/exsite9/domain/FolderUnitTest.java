/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FolderUnitTest
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
        testDirFile.delete();
    }
    
    @Test
    public void testIdentifyNewFilesInvalidFolder()
    {
        String folderName = testDirName + File.separator + "DoesNotExist";
        Folder f = new Folder(new File(folderName));
        List<ResearchFile> newFiles = f.identifyNewFiles();
        assertTrue("List is empty",newFiles.isEmpty());
    }
    
    @Test
    public void testIdentifyNewFilesEmptyFolder()
    {
        Folder f = new Folder(testDirFile);
        List<ResearchFile> newFiles = f.identifyNewFiles();
        assertTrue("List is empty",newFiles.isEmpty());
    }
    
}
