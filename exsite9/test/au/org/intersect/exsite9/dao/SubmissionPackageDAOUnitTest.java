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

import org.junit.Test;

import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;

/**
 * Tests {@link SubmissionPackageDAO}
 */
public final class SubmissionPackageDAOUnitTest extends DAOTest
{

    @Test
    public void testCrud()
    {
        final EntityManager em = createEntityManager();
        final SubmissionPackageDAO toTest = new SubmissionPackageDAO(em);
        final ResearchFileDAO researchFileDAO = new ResearchFileDAO(em);

        assertNull(toTest.findSubmissionPackageById(0l));

        final SubmissionPackage sp = new SubmissionPackage();
        assertNull(sp.getId());
        toTest.createSubmissionPackage(sp);
        assertNotNull(sp.getId());

        assertEquals(sp, toTest.findSubmissionPackageById(sp.getId()));

        final ResearchFile rf = new ResearchFile(new File("someFile"));
        researchFileDAO.createResearchFile(rf);
        sp.getResearchFiles().add(rf);
        toTest.updateSubmissionPackage(sp);

        assertEquals(rf, toTest.findSubmissionPackageById(sp.getId()).getResearchFiles().get(0));
        assertEquals(sp, toTest.getSubmissionPackagesWithResearchFiles(rf).get(0));

        sp.getResearchFiles().clear();
        // perform update inside a transaction
        em.getTransaction().begin();
        toTest.updateSubmissionPackage(sp);
        em.getTransaction().commit();

        assertTrue(toTest.findSubmissionPackageById(sp.getId()).getResearchFiles().isEmpty());
        assertTrue(toTest.getSubmissionPackagesWithResearchFiles(rf).isEmpty());

        toTest.deleteSubmissionPackage(sp);
        assertNull(toTest.findSubmissionPackageById(sp.getId()));
    }
}
