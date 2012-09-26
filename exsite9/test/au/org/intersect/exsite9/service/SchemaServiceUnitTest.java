/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import au.org.intersect.exsite9.dao.DAOTest;
import au.org.intersect.exsite9.dao.factory.MetadataAttributeDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataCategoryDAOFactory;
import au.org.intersect.exsite9.dao.factory.SchemaDAOFactory;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Schema;

/**
 * Tests {@link SchemaService}
 */
public final class SchemaServiceUnitTest extends DAOTest
{

    @Test
    public void testCreateLocalSchema()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        when(emf.createEntityManager()).thenReturn(createEntityManager());

        final File defaultSchemaDir = new File("defaultSchemaDir");
        final File defaultSchemaFile = new File("defaultSchemaFile");
        final File metadataSchemaSchema = new File("metadataSchemaSchema");
        final SchemaDAOFactory schemaDAOFactory = new SchemaDAOFactory();
        final MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();
        final MetadataAttributeDAOFactory metadataAttributeDAOFactory = new MetadataAttributeDAOFactory();
        final SchemaService toTest = new SchemaService(defaultSchemaDir, defaultSchemaFile, metadataSchemaSchema, emf, schemaDAOFactory, metadataCategoryDAOFactory, metadataAttributeDAOFactory);

        assertEquals(defaultSchemaFile, toTest.getDefaultSchema());
        assertEquals(defaultSchemaDir, toTest.getDefaultSchemaDirectory());

        final Schema schema = toTest.createLocalSchema("name", "description", "namespace url");
        assertNotNull(schema.getId());
        assertTrue(schema.getLocal());
        assertEquals("name", schema.getName());
        assertEquals("description", schema.getDescription());
        assertEquals("namespace url", schema.getNamespaceURL());
    }

    @Test
    public void testCreateImportedSchema()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        when(emf.createEntityManager()).thenReturn(createEntityManager());

        final File defaultSchemaDir = new File("defaultSchemaDir");
        final File defaultSchemaFile = new File("defaultSchemaFile");
        final File metadataSchemaSchema = new File("metadataSchemaSchema");
        final SchemaDAOFactory schemaDAOFactory = new SchemaDAOFactory();
        final MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();
        final MetadataAttributeDAOFactory metadataAttributeDAOFactory = new MetadataAttributeDAOFactory();
        final SchemaService toTest = new SchemaService(defaultSchemaDir, defaultSchemaFile, metadataSchemaSchema, emf, schemaDAOFactory, metadataCategoryDAOFactory, metadataAttributeDAOFactory);

        final Schema importedSchema = new Schema("name", "desc", "namespace url", Boolean.FALSE);
        final MetadataCategory mdc = new MetadataCategory("category", MetadataCategoryType.FREETEXT, MetadataCategoryUse.optional);
        final MetadataValue mdv = new MetadataValue("metadata value");
        mdc.getValues().add(mdv);
        importedSchema.getMetadataCategories().add(mdc);

        toTest.createImportedSchema(importedSchema);
        assertNotNull(importedSchema.getId());
    }

    @Test
    public void testUpdateSchema()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final File defaultSchemaDir = new File("defaultSchemaDir");
        final File defaultSchemaFile = new File("defaultSchemaFile");
        final File metadataSchemaSchema = new File("metadataSchemaSchema");
        final SchemaDAOFactory schemaDAOFactory = new SchemaDAOFactory();
        final MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();
        final MetadataAttributeDAOFactory metadataAttributeDAOFactory = new MetadataAttributeDAOFactory();
        final SchemaService toTest = new SchemaService(defaultSchemaDir, defaultSchemaFile, metadataSchemaSchema, emf, schemaDAOFactory, metadataCategoryDAOFactory, metadataAttributeDAOFactory);

        final Schema schema = toTest.createLocalSchema("name", "description", "namespace url");
        assertNotNull(schema.getId());
        assertTrue(schema.getLocal());
        assertEquals("name", schema.getName());
        assertEquals("description", schema.getDescription());
        assertEquals("namespace url", schema.getNamespaceURL());

        toTest.updateSchema(schema, "new name", "new description", "new namespace url");
        final Schema updatedSchema = createEntityManager().find(Schema.class, schema.getId());
        assertEquals("new name", updatedSchema.getName());
        assertEquals("new description", updatedSchema.getDescription());
        assertEquals("new namespace url", updatedSchema.getNamespaceURL());
    }

    @Test
    public void testRemoveSchema()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final File defaultSchemaDir = new File("defaultSchemaDir");
        final File defaultSchemaFile = new File("defaultSchemaFile");
        final File metadataSchemaSchema = new File("metadataSchemaSchema");
        final SchemaDAOFactory schemaDAOFactory = new SchemaDAOFactory();
        final MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();
        final MetadataAttributeDAOFactory metadataAttributeDAOFactory = new MetadataAttributeDAOFactory();
        final SchemaService toTest = new SchemaService(defaultSchemaDir, defaultSchemaFile, metadataSchemaSchema, emf, schemaDAOFactory, metadataCategoryDAOFactory, metadataAttributeDAOFactory);

        final Schema importedSchema = new Schema("name", "desc", "namespace url", Boolean.FALSE);
        final MetadataCategory mdc = new MetadataCategory("category", MetadataCategoryType.FREETEXT, MetadataCategoryUse.optional);
        final MetadataValue mdv = new MetadataValue("metadata value");
        mdc.getValues().add(mdv);
        importedSchema.getMetadataCategories().add(mdc);

        toTest.createImportedSchema(importedSchema);
        assertNotNull(importedSchema.getId());

        toTest.removeSchema(importedSchema);
        assertNull(createEntityManager().find(Schema.class, importedSchema.getId()));
    }

    @Test
    public void testAddRemoveMetadataCategory()
    {
        final EntityManagerFactory emf = mock(EntityManagerFactory.class);
        stub(emf.createEntityManager()).toAnswer(new Answer<EntityManager>()
        {
            @Override
            public EntityManager answer(final InvocationOnMock invocation) throws Throwable
            {
                return createEntityManager();
            }
        });

        final File defaultSchemaDir = new File("defaultSchemaDir");
        final File defaultSchemaFile = new File("defaultSchemaFile");
        final File metadataSchemaSchema = new File("metadataSchemaSchema");
        final SchemaDAOFactory schemaDAOFactory = new SchemaDAOFactory();
        final MetadataCategoryDAOFactory metadataCategoryDAOFactory = new MetadataCategoryDAOFactory();
        final MetadataAttributeDAOFactory metadataAttributeDAOFactory = new MetadataAttributeDAOFactory();
        final SchemaService toTest = new SchemaService(defaultSchemaDir, defaultSchemaFile, metadataSchemaSchema, emf, schemaDAOFactory, metadataCategoryDAOFactory, metadataAttributeDAOFactory);

        final Schema schema = toTest.createLocalSchema("name", "description", "namespace url");
        assertNotNull(schema.getId());

        final MetadataCategory mc = new MetadataCategory("mc", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.required);
        final MetadataValue mv = new MetadataValue("mv");
        mc.getValues().add(mv);

        toTest.addMetadataCategoryToSchema(schema, mc);

        final Schema outSchema1 = createEntityManager().find(Schema.class, schema.getId());
        assertEquals(1, outSchema1.getMetadataCategories().size());

        toTest.removeMetadataCategoryFromSchema(schema, mc);

        final Schema outSchema2 = createEntityManager().find(Schema.class, schema.getId());
        assertEquals(0, outSchema2.getMetadataCategories().size());
    }
}
