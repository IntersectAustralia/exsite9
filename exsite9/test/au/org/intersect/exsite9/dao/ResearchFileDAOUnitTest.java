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

import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Folder;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

public class ResearchFileDAOUnitTest extends DAOTest
{

    private static ResearchFileDAOFactory researchFileDAOFactory;

    @BeforeClass
    public static void setupOnce()
    {
        researchFileDAOFactory = new ResearchFileDAOFactory();
    }

    @Test
    public void testCreateNewFile()
    {
        final EntityManager em = createEntityManager();
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);

        final File fileOnDisk = new File("some-file.txt");
        final ResearchFile researchFile = new ResearchFile(fileOnDisk);

        researchFileDAO.createResearchFile(researchFile);

        assertEquals(researchFile, researchFileDAO.findById(researchFile.getId()));

        em.close();
    }

    @Test
    public void testRemoveResearchFile()
    {
        final EntityManager em = createEntityManager();
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
        final File fileOnDisk = new File("some-file.txt");
        final ResearchFile researchFile = new ResearchFile(fileOnDisk);

        researchFileDAO.createResearchFile(researchFile);
        assertNotNull(researchFile.getId());

        researchFileDAO.removeResearchFile(researchFile);

        assertNull(researchFileDAO.findById(researchFile.getId()));
        em.close();
    }

    @Test
    public void testRemoveResearchFileTrans()
    {
        final EntityManager em = createEntityManager();
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
        final File fileOnDisk = new File("some-file.txt");
        final ResearchFile researchFile = new ResearchFile(fileOnDisk);

        researchFileDAO.createResearchFile(researchFile);
        assertNotNull(researchFile.getId());

        em.getTransaction().begin();
        researchFileDAO.removeResearchFile(researchFile);
        em.getTransaction().commit();

        assertNull(researchFileDAO.findById(researchFile.getId()));
        em.close();
    }

    @Test
    public void testFindByPath()
    {

        final EntityManager em = createEntityManager();
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
        final ProjectDAO projectDAO = new ProjectDAO(em);
        final File fileOnDisk = new File("some-file.txt");
        final ResearchFile researchFile = new ResearchFile(fileOnDisk);
        final Project project = new Project(new ProjectFieldsDTO("name", "owner", "desc", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        researchFile.setProject(project);

        projectDAO.createProject(project);

        assertNull(researchFileDAO.findByPath(project, fileOnDisk));

        researchFileDAO.createResearchFile(researchFile);
        assertNotNull(researchFile.getId());

        assertEquals(researchFile, researchFileDAO.findByPath(project, fileOnDisk));
    }

    @Test
    public void testUpdateResearchFile()
    {
        final EntityManager em = createEntityManager();
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);

        final File file1 = new File("some file on my disk");
        final ResearchFile rf1 = new ResearchFile(file1);

        assertNull(rf1.getId());

        researchFileDAO.createResearchFile(rf1);
        assertNotNull(rf1.getId());

        final File file2 = new File("some other file on my disk");
        rf1.setFile(file2);

        researchFileDAO.updateResearchFile(rf1);
        assertEquals(file2, researchFileDAO.findById(rf1.getId()).getFile());

        // in a transaction
        final File file3 = new File("yet another file on my disk");
        rf1.setFile(file3);

        em.getTransaction().begin();
        researchFileDAO.updateResearchFile(rf1);
        em.getTransaction().commit();
        assertEquals(file3, researchFileDAO.findById(rf1.getId()).getFile());

    }

    @Test
    public void testGetParentFolder()
    {
        final EntityManager em = createEntityManager();
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
        final FolderDAO folderDAO = new FolderDAO(em);

        final File parent = new File("parent");
        final Folder folder = new Folder(parent);

        final File child = new File("child");
        final ResearchFile rf = new ResearchFile(child);

        folder.getFiles().add(rf);

        researchFileDAO.createResearchFile(rf);
        folderDAO.createFolder(folder);

        final Folder outParent = researchFileDAO.getParentFolder(rf);
        assertNotNull(outParent);
        assertEquals(folder, outParent);
    }
}
