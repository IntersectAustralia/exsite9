/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;

import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * Test {@link AlphabeticalMetadataValueComparator}
 */
public class AlphabeticalMetadataValueComparatorUnitTest
{
    @Test
    public void testComparator()
    {
        Comparator<MetadataValue> comparator = new AlphabeticalMetadataValueComparator();

        final MetadataValue valueA = new MetadataValue("abcd");
        final MetadataValue valueZ = new MetadataValue("zyxw");
        final MetadataValue valueAZ = new MetadataValue("azac");

        assertTrue(comparator.compare(valueA, valueZ) < 0);
        assertTrue(comparator.compare(valueA, valueAZ) < 0);
        assertTrue(comparator.compare(valueAZ, valueZ) < 0);
        assertTrue(comparator.compare(valueAZ, valueA) > 0);      
        assertTrue(comparator.compare(valueZ, valueA) > 0);
        assertTrue(comparator.compare(valueZ, valueAZ) > 0);


    }

}
