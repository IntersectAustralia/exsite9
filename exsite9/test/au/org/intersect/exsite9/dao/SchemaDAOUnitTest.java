/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Schema;

/**
 * Tests {@link SchemaDAO}
 */
public final class SchemaDAOUnitTest extends DAOTest
{

    @Test
    public void testCreateSchema()
    {
        final EntityManager em = createEntityManager();
        final SchemaDAO toTest = new SchemaDAO(em);

        final Schema schema = new Schema("name", "desc", "namespaceURL", Boolean.valueOf(false));
        assertNull(schema.getId());

        toTest.createSchema(schema);
        assertNotNull(schema.getId());
    }

    @Test
    public void testUpdateSchema()
    {
        final EntityManager em = createEntityManager();
        final SchemaDAO toTest = new SchemaDAO(em);

        final Schema schema = new Schema("name", "desc", "namespaceURL", Boolean.valueOf(false));
        assertNull(schema.getId());

        toTest.createSchema(schema);
        assertNotNull(schema.getId());

        assertEquals(schema, em.find(Schema.class, schema.getId()));

        final String newDescription = "some new description";
        schema.setDescription(newDescription);
        toTest.updateSchema(schema);

        assertEquals(newDescription, em.find(Schema.class, schema.getId()).getDescription());
    }

    @Test
    public void testDelete()
    {
        final EntityManager em = createEntityManager();
        final SchemaDAO toTest = new SchemaDAO(em);

        final Schema schema = new Schema("name", "desc", "namespaceURL", Boolean.valueOf(false));
        assertNull(schema.getId());

        toTest.createSchema(schema);
        assertNotNull(schema.getId());

        assertEquals(schema, em.find(Schema.class, schema.getId()));

        toTest.delete(schema);

        assertNull(em.find(Schema.class, schema.getId()));
    }
}
