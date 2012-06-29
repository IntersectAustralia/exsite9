package au.org.intersect.exsite9.domain.utils;

import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;

import au.org.intersect.exsite9.domain.MetadataValue;

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
