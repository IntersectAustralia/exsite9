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
 * Tests {@link ResearchFile}
 */
public final class GroupUnitTest
{

    @Test
    public void testConstruction()
    {
        final String n1 = "group 1";

        final Group toTest1 = new Group(n1);

        assertEquals(n1, toTest1.getName());
        assertTrue(toTest1.getGroups().isEmpty());
        assertTrue(toTest1.getFiles().isEmpty());

        final String toString = toTest1.toString();
        assertTrue(toString.contains("name=group 1,groups=[],files=[]"));
    }

    @Test
    public void testEqualsHashCode()
    {
        final String n1 = "group 1";
        final String n2 = "group 2";

        final Group toTest1 = new Group(n1);
        final Group toTest2 = new Group(n1);
        final Group toTest3 = new Group(n2);

        assertEquals(toTest1, toTest1);

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        // Different name
        assertNotEqualsHashCode(toTest1, toTest3);
        assertNotEqualsHashCode(toTest2, toTest3);

        // Different child nodes.
        toTest1.getGroups().add(new Group("some group"));
        assertNotEqualsHashCode(toTest1, toTest2);
        assertNotEqualsHashCode(toTest1, toTest3);

        // Different child files.
        toTest1.getFiles().add(new ResearchFile("some File"));
        assertNotEqualsHashCode(toTest1, toTest2);
        assertNotEqualsHashCode(toTest1, toTest3);

        assertNotEquals(toTest1, null);
        assertNotEquals(toTest1, n1);
        assertNotEquals(toTest1, new Object());
    }
}
