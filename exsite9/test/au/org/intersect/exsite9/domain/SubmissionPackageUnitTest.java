/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link SubmissionPackage} 
 */
public final class SubmissionPackageUnitTest
{

    @Test
    public void testConstruction()
    {
        final SubmissionPackage toTest = new SubmissionPackage();

        assertNull(toTest.getId());
        assertNull(toTest.getName());
        assertNull(toTest.getDescription());
        assertTrue(toTest.getResearchFiles().isEmpty());

        final Long id = 32l;
        final String name = "name";
        final String desc = "desc";
        toTest.setId(id);
        toTest.setName(name);
        toTest.setDescription(desc);

        assertEquals(id, toTest.getId());
        assertEquals(name, toTest.getName());
        assertEquals(desc, toTest.getDescription());
        assertFalse(toTest.toString().isEmpty());
    }

    @Test
    public void testEqualsHashcode()
    {
        final SubmissionPackage toTest1 = new SubmissionPackage();
        toTest1.setId(5l);
        final SubmissionPackage toTest2 = new SubmissionPackage();
        toTest2.setId(5l);

        assertEquals(toTest1, toTest1);
        assertTrue(toTest1.equals(toTest2));
        assertTrue(toTest2.equals(toTest1));
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        toTest2.setId(10l);
        assertFalse(toTest1.equals(toTest2));
        assertFalse(toTest2.equals(toTest1));
        assertNotEquals(toTest1.hashCode(), toTest2.hashCode());

        assertFalse(toTest1.equals(null));
        assertFalse(toTest1.equals(new Object()));
        assertFalse(toTest1.equals(5l));
    }
}
