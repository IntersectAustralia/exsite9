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

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.FieldOfResearchDAOFactory;

/**
 * Tests {@link FieldOfResearchService}
 */
public final class FieldOfResearchServiceUnitTest extends DAOTest
{

    @Test
    public void test()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toReturn(createEntityManager());

        final FieldOfResearchDAOFactory fieldOfResarchDAOFactory = new FieldOfResearchDAOFactory();
        final FieldOfResearchService forService = new FieldOfResearchService(emf, fieldOfResarchDAOFactory);

        assertTrue(forService.getAll().isEmpty());
    }
}
