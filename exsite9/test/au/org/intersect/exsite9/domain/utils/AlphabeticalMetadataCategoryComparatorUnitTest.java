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

import au.org.intersect.exsite9.domain.MetadataCategory;

/**
 * Tests {@link AlphabeticalMetadataCategoryComparator}
 */
public final class AlphabeticalMetadataCategoryComparatorUnitTest
{

    @Test
    public void test()
    {
        final AlphabeticalMetadataCategoryComparator toTest = new AlphabeticalMetadataCategoryComparator();
        assertTrue(toTest instanceof Comparator<?>);

        final MetadataCategory cat1 = new MetadataCategory("cat1");
        final MetadataCategory cat2 = new MetadataCategory("cat2");

        assertTrue(toTest.compare(cat1, cat2) < 0);
        assertTrue(toTest.compare(cat2, cat1) > 0);
    }
}
