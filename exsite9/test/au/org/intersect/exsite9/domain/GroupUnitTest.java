/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

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
        assertTrue(toTest1.getResearchFiles().isEmpty());

        toTest1.setId(Long.valueOf(7));
        assertEquals(Long.valueOf(7), toTest1.getId());

        final String toString = toTest1.toString();
        assertTrue(toString.contains("name=group 1"));
        assertTrue(toString.contains("id=7"));
    }

    @Test
    public void testEqualsHashCode()
    {
        final String n1 = "group 1";
        final String n2 = "group 2";

        final Group toTest1 = new Group(n1);
        toTest1.setId(7l);
        final Group toTest2 = new Group(n1);
        toTest2.setId(7l);
        final Group toTest3 = new Group(n2);
        toTest3.setId(12l);

        assertEquals(toTest1, toTest1);

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        // Different name
        assertNotEquals(toTest1, toTest3);
        assertNotEquals(toTest2, toTest3);

        // Different child nodes.
        toTest1.getGroups().add(new Group("some group"));
        assertEquals(toTest1, toTest2);
        assertNotEquals(toTest1, toTest3);

        // Different child files.
        toTest1.getResearchFiles().add(new ResearchFile(new File("some File")));
        assertEquals(toTest1, toTest2);
        assertNotEquals(toTest1, toTest3);

        assertNotEquals(toTest1, null);
        assertNotEquals(toTest1, n1);
        assertNotEquals(toTest1, new Object());
    }
    
    @Test
    public void testIsAncestorOf()
    {
        Group parent = new Group("Parent One");
        parent.setId(1l);
        Group child = new Group("Child One");
        child.setId(2l);
        Group grandChild = new Group("GrandChild One");
        grandChild.setId(3l);
        
        child.getGroups().add(grandChild);
        parent.getGroups().add(child);
        
        assertTrue(parent.isAnAncestorOf(child));
        assertTrue(parent.isAnAncestorOf(grandChild));
        assertTrue(child.isAnAncestorOf(grandChild));
        
        assertFalse(grandChild.isAnAncestorOf(parent));
        assertFalse(child.isAnAncestorOf(parent));
    }
}
