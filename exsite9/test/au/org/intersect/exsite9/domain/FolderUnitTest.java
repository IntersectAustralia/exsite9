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
import java.util.Arrays;

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
        assertTrue(toTest1.getFolder() == null);
        assertNull(toTest1.getId());
        assertEquals(0, toTest1.getLastCheckTimeInMillis());

        toTest1.setId(Long.valueOf(7));
        assertEquals(Long.valueOf(7), toTest1.getId());

        final long now = System.currentTimeMillis();
        toTest1.setLastCheckTimeInMillis(now);
        assertEquals(now, toTest1.getLastCheckTimeInMillis());

        final ResearchFile rf = new ResearchFile();
        toTest1.setFiles(Arrays.asList(rf));
        assertEquals(1, toTest1.getFiles().size());
        assertEquals(rf, toTest1.getFiles().get(0));

        final File file = new File("/user/path/folder");
        final Folder toTest2 = new Folder(file);
        assertEquals(file.getName(), toTest2.getFolder().getName());
        assertEquals(file.getAbsolutePath(), toTest2.getFolder().getPath());
        assertNull(toTest2.getId());
        assertEquals(0, toTest2.getLastCheckTimeInMillis());
    }

    @Test
    public void testEqualsHashCode()
    {
        final File file = new File("/user/path/file.txt");
        final File fileWithoutPath = new File("file.txt");

        final Folder toTest1 = new Folder(file);
        final Folder toTest2 = new Folder(fileWithoutPath);
   
        assertNotEqualsHashCode(toTest1, toTest2);
        assertFalse(toTest1.equals(null));
        assertNotEqualsHashCode(toTest1, new Object());
        assertNotEqualsHashCode(toTest1, "some string");
    }
}
