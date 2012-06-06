/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * Tests {@link Folder}
 */
public final class FolderUnitTest
{
    @Test
    public void testConstruction()
    {
        final Folder toTest1 = new Folder();
        assertTrue(toTest1.getName().isEmpty());
        assertTrue(toTest1.getPath().isEmpty());
        assertNull(toTest1.getId());
        assertEquals(0, toTest1.getLastCheckTimeInMillis());

        toTest1.setId(Long.valueOf(7));
        assertEquals(Long.valueOf(7), toTest1.getId());

        final long now = System.currentTimeMillis();
        toTest1.setLastCheckTimeInMillis(now);
        assertEquals(now, toTest1.getLastCheckTimeInMillis());

        final Folder toTest2 = new Folder("name", "path");
        assertEquals("name", toTest2.getName());
        assertEquals("path", toTest2.getPath());
        assertNull(toTest2.getId());
        assertEquals(0, toTest2.getLastCheckTimeInMillis());

        final File file = new File("path/file.txt");
        final Folder toTest3 = new Folder(file);
        assertEquals(file.getName(), toTest3.getName());
        assertEquals(file.getAbsolutePath(), toTest3.getPath());
        assertNull(toTest3.getId());
        assertEquals(0, toTest3.getLastCheckTimeInMillis());
    }

    @Test
    public void testEqualsHashCode()
    {
        final String path1 = "path1";
        final String name1 = "name1";

        final Folder toTest1 = new Folder();
        final Folder toTest2 = new Folder();
        final Folder toTest3 = new Folder(name1, "");
        final Folder toTest4 = new Folder(name1, path1);

        assertEquals(toTest1, toTest1);
        assertEquals(toTest1.hashCode(), toTest1.hashCode());

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        assertNotEqualsHashCode(toTest1, toTest3);
        assertNotEqualsHashCode(toTest2, toTest3);

        assertNotEqualsHashCode(toTest1, toTest4);
        assertNotEqualsHashCode(toTest2, toTest4);
        assertNotEqualsHashCode(toTest3, toTest4);

        assertFalse(toTest1.equals(null));
        assertNotEqualsHashCode(toTest1, new Object());
        assertNotEqualsHashCode(toTest1, "some string");
    }
}
