/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;
import static au.org.intersect.exsite9.test.Assert.*;

import org.junit.Test;

/**
 * Tests {@link MetadataCategory}
 */
public final class MetadataCategoryUnitTest
{
    @Test
    public void testConstruction()
    {
        final MetadataCategory toTest1 = new MetadataCategory();
        assertNull(toTest1.getName());
        assertNull(toTest1.getId());
        assertTrue(toTest1.getValues().isEmpty());

        final String name = "name";
        final String newName = "newName";
        final Long id = Long.valueOf(7);
        final MetadataCategory toTest2 = new MetadataCategory(name);
        assertEquals(name, toTest2.getName());
        assertNull(toTest2.getId());
        assertTrue(toTest2.getValues().isEmpty());
        toTest2.setId(id);
        assertEquals(id, toTest2.getId());
        toTest2.setName(newName);
        assertEquals(newName, toTest2.getName());

        assertTrue(toTest1.hashCode() != toTest2.hashCode());
    }

    @Test
    public void testEqualsHashCode()
    {
        final MetadataCategory toTest1 = new MetadataCategory();
        final MetadataCategory toTest2 = new MetadataCategory();

        toTest1.setId(7l);
        toTest2.setId(7l);

        assertEquals(toTest1, toTest1);
        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);

        toTest2.setId(424l);
        assertNotEquals(toTest1, toTest2);
        assertNotEquals(toTest2, toTest1);

        assertFalse(toTest1.equals(new Object()));
        assertFalse(toTest1.equals(null));
        assertFalse(toTest1.equals("some string"));
        assertFalse(toTest1.equals(7l));
    }
}