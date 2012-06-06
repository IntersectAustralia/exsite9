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
}
