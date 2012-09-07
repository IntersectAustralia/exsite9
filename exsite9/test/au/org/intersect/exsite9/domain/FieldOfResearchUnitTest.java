/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link FieldOfResearch}
 */
public final class FieldOfResearchUnitTest
{
    @Test
    public void testConstruction()
    {
        final String code = "someCode";
        final String name = "name name";

        final FieldOfResearch toTest = new FieldOfResearch(code, name);
        assertEquals(code, toTest.getCode());
        assertEquals(name, toTest.getName());
        assertEquals(code + " - " + name, toTest.toString());

        final String newCode = "newCode";
        final String newName = "newName";

        toTest.setCode(newCode);
        toTest.setName(newName);
    }

    @Test
    public void testEqualsHashCode()
    {
        final FieldOfResearch toTest1 = new FieldOfResearch("code1", "name1");
        final FieldOfResearch toTest2 = new FieldOfResearch("code1", "name1");
        final FieldOfResearch toTest3 = new FieldOfResearch("code2", "name2");

        assertEquals(toTest1, toTest1);

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        assertFalse(toTest1.equals(toTest3));
        assertFalse(toTest3.equals(toTest1));
        assertTrue(toTest1.hashCode() != toTest3.hashCode());

        assertFalse(toTest1.equals("code1"));
        assertFalse(toTest1.equals(new Object()));
    }
}
