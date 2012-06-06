/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.junit.Test;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Tests {@link AlphabeticalResearchFileComparator}
 */
public final class AlphabeticalResearchFileComparatorUnitTest
{

    @Test
    public void testComparator()
    {
        final Comparator<ResearchFile> toTest = new AlphabeticalResearchFileComparator();

        final ResearchFile rf1 = new ResearchFile("abcd.txt");
        final ResearchFile rf2 = new ResearchFile("zxyw.txt");
        final ResearchFile rf3 = new ResearchFile("abcd.txt");

        assertTrue(toTest.compare(rf1, rf2) <= -1);
        assertTrue(toTest.compare(rf2, rf1) >= 1);

        rf1.setId(Long.valueOf(1));
        rf3.setId(Long.valueOf(2));

        assertTrue(toTest.compare(rf1, rf1) == 0);

        assertTrue(toTest.compare(rf1, rf3) <= -1);
        assertTrue(toTest.compare(rf3, rf1) >= 1);
    }
}
