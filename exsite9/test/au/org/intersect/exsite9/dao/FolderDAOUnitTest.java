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
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.domain.Folder;

public class FolderDAOUnitTest extends DAOTest
{
    private static FolderDAOFactory folderDAOFactory = null;
    
    @BeforeClass
    public static void setupOnce()
    {
        folderDAOFactory = new FolderDAOFactory();
    }
    
    @Test
    public void createNewFolderTest()
    {
        EntityManager em = createEntityManager();
        FolderDAO folderDAO = folderDAOFactory.createInstance(em);

        File folderOnDisk = new File("/tmp");
        Folder tempFolder = new Folder(folderOnDisk);
        folderDAO.createFolder(tempFolder);
        
        em.close();
    }
}
