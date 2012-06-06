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

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.NewFilesGroup;

/**
 * Tests {@link AlphabeticalGroupComparator}
 */
public final class AlphabeticalGroupComparatorUnitTest
{
    @Test
    public void testComparator()
    {
        final Comparator<Group> toTest = new AlphabeticalGroupComparator();

        final Group group1 = new Group("abcd");
        final Group group2 = new Group("zyxw");
        final Group group3 = new NewFilesGroup();

        assertEquals(1, toTest.compare(group3, group1));
        assertEquals(1, toTest.compare(group3, group2));

        assertEquals(-1, toTest.compare(group1, group3));
        assertEquals(-1, toTest.compare(group2, group3));

        assertTrue(toTest.compare(group1, group2) <= -1);
        assertTrue(toTest.compare(group2, group1) >= 1);
    }
}
