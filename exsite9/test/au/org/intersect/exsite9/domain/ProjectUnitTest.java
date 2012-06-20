/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.assertNotEquals;
import static au.org.intersect.exsite9.test.Assert.assertNotEqualsHashCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

/**
 * Tests {@link Project}
 */
public final class ProjectUnitTest
{

    @Test
    public void testConstruction()
    {
        final String n1 = "project 1";
        final String o1 = "owner 1";
        final String d1 = "Project One";

        final Project toTest1 = new Project(n1,o1,d1);

        assertEquals(n1, toTest1.getName());
        assertTrue(toTest1.getRootNode().getGroups().size() == 1);
        assertTrue(toTest1.getRootNode().getResearchFiles().isEmpty());
        assertEquals(d1, toTest1.getDescription());
        assertEquals(o1, toTest1.getOwner());
        assertTrue(toTest1.getMetadataCategories().isEmpty());

        final String toString = toTest1.toString();
       
        assertTrue(toString.contains("name=project 1"));
        assertTrue(toString.contains("description=Project One"));
        assertTrue(toString.contains("owner=owner 1"));

        toTest1.setFolders(null);
        toTest1.setMetadataCategories(null);
        assertNull(toTest1.getFolders());
        assertNull(toTest1.getMetadataCategories());
    }

    @Test
    public void testEqualsHashCode()
    {
        final String n1 = "project 1";
        final String o1 = "owner 1";
        final String d1 = "Project One";
        final String n2 = "project 2";
        final String o2 = "owner 2";
        final String d2 = "Project Two";

        final Project toTest1 = new Project(n1,o1,d1);
        final Project toTest2 = new Project(n1,o1,d1);
        final Project toTest3 = new Project(n2,o2,d2);

        assertEquals(toTest1, toTest1);

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        // Different name
        assertNotEqualsHashCode(toTest1, toTest3);
        assertNotEqualsHashCode(toTest2, toTest3);

        // Different child nodes.
        toTest1.getRootNode().getGroups().add(new Group("some group"));
        assertNotEquals(toTest1, toTest2);
        assertNotEquals(toTest1, toTest3);

        // Different child files.
        toTest1.getRootNode().getResearchFiles().add(new ResearchFile(new File("some File")));
        assertNotEquals(toTest1, toTest2);
        assertNotEquals(toTest1, toTest3);

        assertNotEquals(toTest1, null);
        assertNotEquals(toTest1, new Object());
        assertNotEquals(toTest1, n1);
    }
}
