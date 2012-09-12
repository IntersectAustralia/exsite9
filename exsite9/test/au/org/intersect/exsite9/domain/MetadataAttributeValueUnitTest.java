/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link MetadataAttributeValue}
 */
public final class MetadataAttributeValueUnitTest
{
    @Test
    public void testConstruction()
    {
        final String value = "someValue";
        final MetadataAttributeValue toTest1 = new MetadataAttributeValue(value);
        assertEquals(value, toTest1.getValue());

        final Long id = Long.valueOf(74);
        toTest1.setId(id);
        assertEquals(id, toTest1.getId());

        final String newValue = "newValue";
        toTest1.setValue(newValue);
        assertEquals(newValue, toTest1.getValue());

        assertNotNull(toTest1.toString());
    }

    @Test
    public void testEqualsHashCode()
    {
        final String value1 = "someValue";
        final MetadataAttributeValue toTest1 = new MetadataAttributeValue(value1);
        final MetadataAttributeValue toTest2 = new MetadataAttributeValue(value1);

        assertEquals(toTest1, toTest1);
        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        assertFalse(toTest1.equals(value1));
        assertFalse(toTest1.equals(""));
        assertFalse(toTest1.equals(null));
        assertFalse(toTest1.equals(new Object()));
    }
}
