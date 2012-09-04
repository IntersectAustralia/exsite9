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
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;

/**
 * Tests {@link IDMetadataCategoryComparator}
 */
public final class IDMetadataCategoryComparatorUnitTest
{

    @Test
    public void test()
    {
        final Comparator<MetadataCategory> toTest = new IDMetadataCategoryComparator();

        final MetadataCategory cat1 = new MetadataCategory("cat1", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataCategory cat2 = new MetadataCategory("cat2", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        cat1.setId(100l);
        cat2.setId(200l);

        assertTrue(toTest.compare(cat1, cat2) < 0);
        assertTrue(toTest.compare(cat2, cat1) > 0);
        assertTrue(toTest.compare(cat1, cat1) == 0);
        assertTrue(toTest.compare(cat2, cat2) == 0);
    }
}
