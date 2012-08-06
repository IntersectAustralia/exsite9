/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Tests {@link Schema}
 */
public final class SchemaUnitTest
{
    @Test
    public void testSchema()
    {
        final Schema schema = new Schema("name", "desc", "namespace URL", Boolean.TRUE);

        assertNull(schema.getId());
        assertEquals("name", schema.getName());
        assertEquals("desc", schema.getDescription());
        assertEquals("namespace URL", schema.getNamespaceURL());
        assertEquals(Boolean.TRUE, schema.getLocal());
        assertTrue(schema.getMetadataCategories().isEmpty());

        schema.setId(47l);
        schema.setName("new name");
        schema.setDescription("new description");
        schema.setNamespaceURL("new namespace url");
        schema.setLocal(Boolean.FALSE);
        schema.setMetadataCategories(new ArrayList<MetadataCategory>());

        assertEquals(Long.valueOf(47l), schema.getId());
        assertEquals("new name", schema.getName());
        assertEquals("new description", schema.getDescription());
        assertEquals("new namespace url", schema.getNamespaceURL());
        assertEquals(Boolean.FALSE, schema.getLocal());
        assertTrue(schema.getMetadataCategories().isEmpty());

        assertNotNull(schema.toString());
    }

    @Test
    public void testEqualsHashcode()
    {
        final Schema schema1 = new Schema();
        final Schema schema2 = new Schema();

        schema1.setId(47l);
        schema2.setId(47l);

        assertEquals(schema1, schema1);
        assertTrue(schema1.equals(schema2));
        assertTrue(schema2.equals(schema1));
        assertEquals(schema1.hashCode(), schema2.hashCode());

        schema1.setId(1234l);
        assertFalse(schema1.equals(schema2));
        assertFalse(schema2.equals(schema1));
        assertTrue(schema1.hashCode() != schema2.hashCode());

        assertFalse(schema1.equals(null));
        assertFalse(schema1.equals(new Object()));
        assertFalse(schema1.equals("new schema"));
    }
}
