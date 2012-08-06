/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.ProjectDAOFactory;
import au.org.intersect.exsite9.dao.factory.SubmissionPackageDAOFactory;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link SubmissionPackageService}
 */
public final class SubmissionPackageServiceUnitTest extends DAOTest
{
    @Test
    public void testCreateSubmissionPackage()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final SubmissionPackageService toTest = new SubmissionPackageService(emf, submissionPackageDAOFactory, projectDAOFactory, groupDAOFactory);

        final Project project = new Project(new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        final SubmissionPackage out1 = toTest.createSubmissionPackage(project, "name", "description", Collections.<ResearchFile>emptyList());
        assertNotNull(out1.getId());
        assertEquals("name", out1.getName());
        assertEquals("description", out1.getDescription());
        assertTrue(out1.getResearchFiles().isEmpty());
    }

    @Test
    public void testUpdateSubmissionPackage1()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final SubmissionPackageService toTest = new SubmissionPackageService(emf, submissionPackageDAOFactory, projectDAOFactory, groupDAOFactory);

        final Project project = new Project(new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        final SubmissionPackage out1 = toTest.createSubmissionPackage(project, "name", "description", Collections.<ResearchFile>emptyList());
        assertNotNull(out1.getId());
        assertEquals("name", out1.getName());
        assertEquals("description", out1.getDescription());
        assertTrue(out1.getResearchFiles().isEmpty());

        toTest.updateSubmissionPackage(out1, "new name", "new description", Collections.<ResearchFile>emptyList());
        assertEquals("new name", out1.getName());
        assertEquals("new description", out1.getDescription());
        assertTrue(out1.getResearchFiles().isEmpty());
    }

    @Test
    public void testUpdateSubmissionPackage2()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final SubmissionPackageService toTest = new SubmissionPackageService(emf, submissionPackageDAOFactory, projectDAOFactory, groupDAOFactory);

        final Project project = new Project(new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        final SubmissionPackage out1 = toTest.createSubmissionPackage(project, "name", "description", Collections.<ResearchFile>emptyList());
        assertNotNull(out1.getId());
        assertEquals("name", out1.getName());
        assertEquals("description", out1.getDescription());
        assertTrue(out1.getResearchFiles().isEmpty());

        final ResearchFile rf1 = new ResearchFile(new File("rf1"));
        final ResearchFile rf2 = new ResearchFile(new File("rf2"));

        toTest.updateSubmissionPackage(out1, Arrays.asList(rf1, rf2));

        assertEquals(2, out1.getResearchFiles().size());
    }

    @Test
    public void testFindSubmissionPackageById()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final SubmissionPackageService toTest = new SubmissionPackageService(emf, submissionPackageDAOFactory, projectDAOFactory, groupDAOFactory);

        final Project project = new Project(new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        final SubmissionPackage out1 = toTest.createSubmissionPackage(project, "name", "description", Collections.<ResearchFile>emptyList());
        assertNotNull(out1.getId());
        assertEquals("name", out1.getName());
        assertEquals("description", out1.getDescription());
        assertTrue(out1.getResearchFiles().isEmpty());

        assertEquals(out1, toTest.findSubmissionPackageById(out1.getId()));
    }

    @Test
    public void testDeleteSubmissionPackage()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final SubmissionPackageService toTest = new SubmissionPackageService(emf, submissionPackageDAOFactory, projectDAOFactory, groupDAOFactory);

        final Project project = new Project(new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        final SubmissionPackage out1 = toTest.createSubmissionPackage(project, "name", "description", Collections.<ResearchFile>emptyList());
        assertNotNull(out1.getId());
        assertEquals("name", out1.getName());
        assertEquals("description", out1.getDescription());
        assertTrue(out1.getResearchFiles().isEmpty());

        toTest.deleteSubmissionPackage(out1);

        assertNull(toTest.findSubmissionPackageById(out1.getId()));
    }

    @Test
    public void testBuildXMLForSubmissionPacakage()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final SubmissionPackageService toTest = new SubmissionPackageService(emf, submissionPackageDAOFactory, projectDAOFactory, groupDAOFactory);

        final Project project = new Project(new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        final SubmissionPackage out1 = toTest.createSubmissionPackage(project, "name", "description", Collections.<ResearchFile>emptyList());
        assertNotNull(out1.getId());
        assertEquals("name", out1.getName());
        assertEquals("description", out1.getDescription());
        assertTrue(out1.getResearchFiles().isEmpty());

        final String stringOut = toTest.buildXMLForSubmissionPackage(project, out1);
        assertNotNull(stringOut);
        assertFalse(stringOut.isEmpty());
    }

    @Test
    public void testBuildZIPforSubmissionPackage()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final SubmissionPackageService toTest = new SubmissionPackageService(emf, submissionPackageDAOFactory, projectDAOFactory, groupDAOFactory);

        final Project project = new Project(new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        final SubmissionPackage out1 = toTest.createSubmissionPackage(project, "name", "description", Collections.<ResearchFile>emptyList());
        assertNotNull(out1.getId());
        assertEquals("name", out1.getName());
        assertEquals("description", out1.getDescription());
        assertTrue(out1.getResearchFiles().isEmpty());

        final IRunnableWithProgress runnable = toTest.buildZIPForSubmissionPackage(project, out1, new File("some file to write"));
        assertNotNull(runnable);
    }

    @Test
    public void testFindSubmissionPackagesWithResearchFile()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final SubmissionPackageDAOFactory submissionPackageDAOFactory = new SubmissionPackageDAOFactory();
        final ProjectDAOFactory projectDAOFactory = new ProjectDAOFactory();
        final GroupDAOFactory groupDAOFactory = new GroupDAOFactory();
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(createEntityManager());
        final SubmissionPackageService toTest = new SubmissionPackageService(emf, submissionPackageDAOFactory, projectDAOFactory, groupDAOFactory);

        final ResearchFile rf = new ResearchFile(new File("rf"));
        researchFileDAO.createResearchFile(rf);
        assertNotNull(rf.getId());

        final Project project = new Project(new ProjectFieldsDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        final SubmissionPackage out1 = toTest.createSubmissionPackage(project, "name", "description", Arrays.asList(rf));
        assertNotNull(out1.getId());

        final List<SubmissionPackage> matches = toTest.findSubmissionPackagesWithResearchFile(rf);
        assertEquals(1, matches.size());
        assertEquals(out1, matches.get(0));
    }
}
