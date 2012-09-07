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
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Tests {@link ResearchFileSizeComparator}
 */
public final class ResearchFileSizeComparatorUnitTest
{
    @Test
    public void testAsc()
    {
        final Comparator<ResearchFile> toTest = new ResearchFileSizeComparator(true);

        final File f1 = Mockito.mock(File.class);
        final File f2 = Mockito.mock(File.class);

        when(f1.length()).thenReturn(100l);
        when(f2.length()).thenReturn(200l);

        final ResearchFile rf1 = new ResearchFile(f1);
        final ResearchFile rf2 = new ResearchFile(f2);

        assertTrue(toTest.compare(rf1, rf2) == -1);
        assertTrue(toTest.compare(rf2, rf1) == 1);

        assertTrue(toTest.compare(rf1, rf1) == 0);
    }

    @Test
    public void testDesc()
    {
        final Comparator<ResearchFile> toTest = new ResearchFileSizeComparator(false);

        final File f1 = Mockito.mock(File.class);
        final File f2 = Mockito.mock(File.class);

        when(f1.length()).thenReturn(100l);
        when(f2.length()).thenReturn(200l);

        final ResearchFile rf1 = new ResearchFile(f1);
        final ResearchFile rf2 = new ResearchFile(f2);

        assertTrue(toTest.compare(rf1, rf2) == 1);
        assertTrue(toTest.compare(rf2, rf1) == -1);

        assertTrue(toTest.compare(rf1, rf1) == 0);
    }
}
