/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link MetadataValue}
 */
public final class MetadataValueUnitTest
{
    @Test
    public void testConstruction()
    {
        final String value = "someValue";
        final MetadataValue toTest1 = new MetadataValue(value);
        assertEquals(value, toTest1.getValue());

        final Long id = Long.valueOf(74);
        toTest1.setId(id);
        assertEquals(id, toTest1.getId());

        final String newValue = "newValue";
        toTest1.setValue(newValue);
        assertEquals(newValue, toTest1.getValue());
    }

    @Test
    public void testEqualsHashCode()
    {
        final String value1 = "someValue";
        final MetadataValue toTest1 = new MetadataValue(value1);
        final MetadataValue toTest2 = new MetadataValue(value1);
        final MetadataValue toTest3 = new MetadataValue("some other value");

        assertEquals(toTest1, toTest1);
        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        assertNotEqualsHashCode(toTest3, toTest1);

        assertFalse(toTest1.equals(value1));
        assertFalse(toTest1.equals(""));
        assertFalse(toTest1.equals(null));
        assertFalse(toTest1.equals(new Object()));
    }
}
