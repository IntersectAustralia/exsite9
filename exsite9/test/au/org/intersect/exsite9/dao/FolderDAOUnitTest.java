/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.dao;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.domain.Folder;

public class FolderDAOUnitTest extends JPATest
{
    private static FolderDAO folderDAO = null;
    private static EntityManager em;
    
    @BeforeClass
    public static void setupOnce()
    {
        em = createEntityManager();
        folderDAO = FolderDAO.getInstance(em);
    }
    
    @Test
    public void createNewFolderTest()
    {
        File folderOnDisk = new File("/tmp");
        Folder tempFolder = new Folder(folderOnDisk);
        
        folderDAO.createFolder(tempFolder);
    }
}
