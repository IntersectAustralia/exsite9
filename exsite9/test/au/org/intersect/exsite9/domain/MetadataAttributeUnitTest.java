/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.assertNotEquals;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Tests {@link MetadataAttribute}
 */
public final class MetadataAttributeUnitTest
{
    @Test
    public void testConstruction()
    {
        final String name = "name";
        final MetadataAttributeValue val1 = new MetadataAttributeValue("val1");
        final MetadataAttribute toTest = new MetadataAttribute(name, Arrays.asList(val1));

        assertEquals(name, toTest.getName());
        assertEquals(Arrays.asList(val1), toTest.getMetadataAttributeValues());
        assertNull(toTest.getId());

        final String newName = "someNewName";
        toTest.setName(newName);
        assertEquals(newName, toTest.getName());

        final List<MetadataAttributeValue> newValues = Arrays.asList(val1, val1);
        toTest.setMetadataAttributeValues(newValues);
        assertEquals(newValues, toTest.getMetadataAttributeValues());

        final Long newId = 123l;
        toTest.setId(newId);
        assertEquals(newId, toTest.getId());

        assertNotNull(toTest.toString());
    }

    @Test
    public void testEqualsHashCode()
    {
        final MetadataAttribute toTest1 = new MetadataAttribute();
        final MetadataAttribute toTest2 = new MetadataAttribute();

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
