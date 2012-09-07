/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Comparator;

import org.junit.Test;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Tests {@link ResearchFileNameComparator}
 */
public final class ResearchFileNameComparatorUnitTest
{

    @Test
    public void testAsc()
    {
        final Comparator<ResearchFile> toTest = new ResearchFileNameComparator(true);

        final ResearchFile rf1 = new ResearchFile(new File("abcd.txt"));
        final ResearchFile rf2 = new ResearchFile(new File("zxyw.txt"));
        final ResearchFile rf3 = new ResearchFile(new File("abcd.txt"));

        assertTrue(toTest.compare(rf1, rf2) < -1);
        assertTrue(toTest.compare(rf2, rf1) > 1);

        assertTrue(toTest.compare(rf1, rf1) == 0);

        assertTrue(toTest.compare(rf1, rf3) == 0);
        assertTrue(toTest.compare(rf3, rf1) == 0);
    }

    @Test
    public void testDesc()
    {
        final Comparator<ResearchFile> toTest = new ResearchFileNameComparator(false);

        final ResearchFile rf1 = new ResearchFile(new File("abcd.txt"));
        final ResearchFile rf2 = new ResearchFile(new File("zxyw.txt"));
        final ResearchFile rf3 = new ResearchFile(new File("abcd.txt"));

        assertTrue(toTest.compare(rf1, rf2) > 1);
        assertTrue(toTest.compare(rf2, rf1) < -1);

        assertTrue(toTest.compare(rf1, rf1) == 0);

        assertTrue(toTest.compare(rf1, rf3) == 0);
        assertTrue(toTest.compare(rf3, rf1) == 0);
    }
}
