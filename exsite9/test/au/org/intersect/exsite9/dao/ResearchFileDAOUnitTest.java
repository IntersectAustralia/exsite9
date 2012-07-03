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
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

public class ResearchFileDAOUnitTest extends DAOTest
{

    private static ResearchFileDAOFactory researchFileDAOFactory;
    private static String EMPTY_STRING = "";

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
        final Project project = new Project(new ProjectFieldsDTO("name", "owner", "desc", EMPTY_STRING, EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING));
        researchFile.setProject(project);

        projectDAO.createProject(project);

        assertNull(researchFileDAO.findByPath(project, fileOnDisk));

        researchFileDAO.createResearchFile(researchFile);
        assertNotNull(researchFile.getId());

        assertEquals(researchFile, researchFileDAO.findByPath(project, fileOnDisk));
    }
}
