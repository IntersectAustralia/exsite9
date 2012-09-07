/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;

import au.org.intersect.exsite9.domain.FieldOfResearch;

/**
 * Tests {@link FieldOfResearchDAO}
 */
public final class FieldOfResearchDAOUnitTest extends DAOTest
{

    @Test
    public void test()
    {
        final EntityManager em = createEntityManager();
        final FieldOfResearchDAO toTest = new FieldOfResearchDAO(em);

        assertTrue(toTest.getAll().isEmpty());

        final FieldOfResearch for1 = new FieldOfResearch("code1", "name1");
        final FieldOfResearch for2 = new FieldOfResearch("code2", "name2");

        em.getTransaction().begin();
        em.persist(for1);
        em.persist(for2);
        em.getTransaction().commit();

        final List<FieldOfResearch> out = toTest.getAll();
        assertEquals(2, out.size());
        assertTrue(out.contains(for1));
        assertTrue(out.contains(for2));
    }
}
