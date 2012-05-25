/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.manager;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Folder;

public class FolderManagerUnitTest extends JPATest
{
    private FolderManager folderManager = null;
    
    @Test
    public void createNewFolderTest()
    {
        EntityManager em = createEntityManager();
        
        File folderOnDisk = new File("/tmp");
        Folder tempFolder = new Folder(folderOnDisk);
        
        folderManager = new FolderManager(em);
        folderManager.createFolder(tempFolder);
        
        em.close();
    }
}
