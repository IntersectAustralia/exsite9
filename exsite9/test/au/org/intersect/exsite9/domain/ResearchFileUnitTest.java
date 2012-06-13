/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;
import static au.org.intersect.exsite9.test.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * Tests {@link ResearchFile}
 */
public final class ResearchFileUnitTest
{

    @Test
    public void testConstruction()
    {
        final File file1 = new File("/tmp1/filename1");
        final File file2 = new File("/tmp2/filename2");

        final ResearchFile toTest1 = new ResearchFile(file1);
        final ResearchFile toTest2 = new ResearchFile(file1);
        final ResearchFile toTest3 = new ResearchFile(file2);

        assertEquals(toTest1, toTest1);
        assertEquals(file1, toTest1.getFile());
        assertEquals(0, toTest1.getProjectID());

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        assertNotEqualsHashCode(toTest2, toTest3);

        assertNotEquals(toTest1, null);
        assertNotEquals(toTest1, new Object());
        assertNotEquals(toTest1, file1);

        final Long id = Long.valueOf(72121);
        toTest1.setId(id);
        assertEquals(id, toTest1.getId());

        final long projId = 7447;
        toTest1.setProjectID(projId);
        assertEquals(projId, toTest1.getProjectID());

        final String toString = toTest1.toString();
        assertTrue(toString.contains("file=" + file1));
    }
}
