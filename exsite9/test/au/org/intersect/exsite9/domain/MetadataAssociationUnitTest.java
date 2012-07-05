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
 * Tests {@link MetadataAssociation}
 */
public final class MetadataAssociationUnitTest
{

    @Test
    public void testConstruction()
    {
        final MetadataAssociation toTest1 = new MetadataAssociation();

        assertNull(toTest1.getId());
        assertNull(toTest1.getMetadataCategory());
        assertTrue(toTest1.getMetadataValues().isEmpty());

        final Long id = 124l;
        final MetadataCategory mc = new MetadataCategory("some category");
        toTest1.setId(id);
        toTest1.setMetadataCategory(mc);
        assertEquals(id, toTest1.getId());
        assertEquals(mc, toTest1.getMetadataCategory());
        assertTrue(toTest1.getMetadataValues().isEmpty());
        assertFalse(toTest1.toString().isEmpty());
    }

    @Test
    public void testEqualsHashCode()
    {
        final Long id1 = 124l;
        final Long id2 = 234l;
        final MetadataAssociation toTest1 = new MetadataAssociation();
        toTest1.setId(id1);

        final MetadataAssociation toTest2 = new MetadataAssociation();
        toTest2.setId(id1);

        assertEquals(toTest1, toTest1);
        assertTrue(toTest1.equals(toTest2));
        assertTrue(toTest2.equals(toTest1));
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        toTest2.setId(id2);
        assertFalse(toTest1.equals(toTest2));
        assertFalse(toTest2.equals(toTest1));
        assertNotEquals(toTest1.hashCode(), toTest2.hashCode());

        assertFalse(toTest1.equals(new Object()));
        assertFalse(toTest1.equals(null));
        assertFalse(toTest1.equals(id1));
        assertFalse(toTest1.equals(id2));
    }
}
