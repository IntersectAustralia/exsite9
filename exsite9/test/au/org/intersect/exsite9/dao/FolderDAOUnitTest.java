/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.dao;

import static org.junit.Assert.*;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.FolderDAO;
import au.org.intersect.exsite9.dao.factory.FolderDAOFactory;
import au.org.intersect.exsite9.domain.Folder;

/**
 * Tests {@link FolderDAO}
 */
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
        assertNull(tempFolder.getId());
        folderDAO.createFolder(tempFolder);
        assertNotNull(tempFolder.getId());
        assertEquals(tempFolder, em.find(Folder.class, tempFolder.getId()));
        em.close();
    }

    @Test
    public void testUpdateFolder()
    {
        // Create a new folder.
        final File folderOnDisk = new File("someFolder");
        final Folder tempFolder = new Folder(folderOnDisk);

        final EntityManager em = createEntityManager();
        final FolderDAO folderDAO = folderDAOFactory.createInstance(em);

        folderDAO.createFolder(tempFolder);
        assertNotNull(tempFolder.getId());

        final long lastCheckTime = tempFolder.getLastCheckTimeInMillis();
        final long newLastCheckTime = lastCheckTime + 100000;

        tempFolder.setLastCheckTimeInMillis(newLastCheckTime);
        folderDAO.updateFolder(tempFolder);

        assertEquals(newLastCheckTime, em.find(Folder.class, tempFolder.getId()).getLastCheckTimeInMillis());

        em.close();
    }

    @Test
    public void testUpdateFolderInTransaction()
    {
     // Create a new folder.
        final File folderOnDisk = new File("someFolderInATrans");
        final Folder tempFolder = new Folder(folderOnDisk);

        final EntityManager em = createEntityManager();
        final FolderDAO folderDAO = folderDAOFactory.createInstance(em);

        folderDAO.createFolder(tempFolder);
        assertNotNull(tempFolder.getId());

        final long lastCheckTime = tempFolder.getLastCheckTimeInMillis();
        final long newLastCheckTime = lastCheckTime + 100000;
        em.getTransaction().begin();

        tempFolder.setLastCheckTimeInMillis(newLastCheckTime);
        folderDAO.updateFolder(tempFolder);

        em.getTransaction().commit();

        assertEquals(newLastCheckTime, em.find(Folder.class, tempFolder.getId()).getLastCheckTimeInMillis());

        em.close();

    }

    @Test
    public void testRemoveFolder()
    {
        final File folderOnDisk = new File("someOtherFolder");
        final Folder tempFolder = new Folder(folderOnDisk);

        final EntityManager em = createEntityManager();
        final FolderDAO folderDAO = folderDAOFactory.createInstance(em);

        folderDAO.createFolder(tempFolder);
        assertNotNull(tempFolder.getId());

        folderDAO.removeFolder(tempFolder);
        assertNotNull(tempFolder.getId());

        assertNull(em.find(Folder.class, tempFolder.getId()));

        em.close();
    }
}
