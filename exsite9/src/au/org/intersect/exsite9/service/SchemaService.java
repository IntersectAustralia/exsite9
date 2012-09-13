/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import au.org.intersect.exsite9.dao.MetadataAttributeDAO;
import au.org.intersect.exsite9.dao.MetadataCategoryDAO;
import au.org.intersect.exsite9.dao.SchemaDAO;
import au.org.intersect.exsite9.dao.factory.MetadataAttributeDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataCategoryDAOFactory;
import au.org.intersect.exsite9.dao.factory.SchemaDAOFactory;
import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.exception.InvalidSchemaException;
import au.org.intersect.exsite9.xml.MetadataSchemaXMLReader;

/**
 * Handles working with Metadata Schemas.
 */
public final class SchemaService implements ISchemaService
{
    private final EntityManagerFactory emf;
    private final SchemaDAOFactory schemaDAOFactory;
    private final MetadataCategoryDAOFactory metadataCategoryDAOFactory;
    private final MetadataAttributeDAOFactory metadataAttributeDAOFactory;
    private final File defaultSchemaDirectory;
    private final File defaultSchema;
    private final File metadataSchemaSchema;

    /**
     * @param defaultSchemaDirectory The default directory that metadata schema's will lie in.
     * @param defaultSchema The default metadata schema - loaded by default for new projects.
     * @param metadataSchemaSchema The RELAX-NG format schema, used to validate metadata schemas.
     * @param emf
     * @param schemaDAOFactory
     */
    public SchemaService(final File defaultSchemaDirectory, final File defaultSchema, final File metadataSchemaSchema, final EntityManagerFactory emf,
                         final SchemaDAOFactory schemaDAOFactory, final MetadataCategoryDAOFactory metadataCategoryDAOFactory,
                         final MetadataAttributeDAOFactory metadataAttributeDAOFactory)
    {
        this.emf = emf;
        this.schemaDAOFactory = schemaDAOFactory;
        this.defaultSchema = defaultSchema;
        this.metadataCategoryDAOFactory = metadataCategoryDAOFactory;
        this.defaultSchemaDirectory = defaultSchemaDirectory;
        this.metadataSchemaSchema = metadataSchemaSchema;
        this.metadataAttributeDAOFactory = metadataAttributeDAOFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema createLocalSchema(final String schemaName, final String schemaDescription, final String schemaNamespaceURL)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final SchemaDAO schemaDAO = this.schemaDAOFactory.createInstance(em);
            final Schema schema = new Schema(schemaName, schemaDescription, schemaNamespaceURL, Boolean.TRUE);
            schemaDAO.createSchema(schema);
            return schema;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createImportedSchema(final Schema schema)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final MetadataCategoryDAO metadataCategoryDAO = this.metadataCategoryDAOFactory.createInstance(em);
            final MetadataAttributeDAO metadataAttribtueDAO = this.metadataAttributeDAOFactory.createInstance(em);

            for (final MetadataCategory metadataCategory : schema.getMetadataCategories())
            {
                final MetadataAttribute metadataAttribute = metadataCategory.getMetadataAttribute();
                if (metadataAttribute != null)
                {
                    metadataAttribtueDAO.createMetadataAttribute(metadataAttribute);
                }
                metadataCategoryDAO.createMetadataCategory(metadataCategory);
            }

            final SchemaDAO schemaDAO = this.schemaDAOFactory.createInstance(em);
            schemaDAO.createSchema(schema);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void updateSchema(final Schema schema, final String schemaName, final String schemaDescription, final String schemaNamespaceURL)
    {
        schema.setName(schemaName);
        schema.setDescription(schemaDescription);
        schema.setNamespaceURL(schemaNamespaceURL);

        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final SchemaDAO schemaDAO = this.schemaDAOFactory.createInstance(em);
            schemaDAO.updateSchema(schema);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void removeSchema(final Schema schema)
    {
        final EntityManager em = this.emf.createEntityManager();

        try
        {
            final List<MetadataCategory> mdcsToDelete = new ArrayList<MetadataCategory>(schema.getMetadataCategories());

            final SchemaDAO schemaDAO = this.schemaDAOFactory.createInstance(em);
            schema.getMetadataCategories().clear();
            schemaDAO.delete(schema);

            final MetadataCategoryDAO metadataCategoryDAO = this.metadataCategoryDAOFactory.createInstance(em);
            final MetadataAttributeDAO metadataAttributeDAO = this.metadataAttributeDAOFactory.createInstance(em);

            for (final MetadataCategory mdc : mdcsToDelete)
            {
                metadataCategoryDAO.delete(mdc);

                final MetadataAttribute mda = mdc.getMetadataAttribute();
                if (mda != null)
                {
                    metadataAttributeDAO.delete(mda);
                }
            }
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addMetadataCategoryToSchema(final Schema schema, final MetadataCategory metadataCategory)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final SchemaDAO schemaDAO = this.schemaDAOFactory.createInstance(em);
            schema.getMetadataCategories().add(metadataCategory);
            schemaDAO.updateSchema(schema);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void removeMetadataCategoryFromSchema(final Schema schema, final MetadataCategory metadataCategory)
    {
        final EntityManager em = this.emf.createEntityManager();
        try
        {
            final SchemaDAO schemaDAO = this.schemaDAOFactory.createInstance(em);
            schema.getMetadataCategories().remove(metadataCategory);
            schemaDAO.updateSchema(schema);
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public File getDefaultSchemaDirectory()
    {
        return this.defaultSchemaDirectory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getDefaultSchema()
    {
        return this.defaultSchema;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Schema parseSchema(final File xmlFile) throws SAXException, IOException, ParserConfigurationException, InvalidSchemaException
    {
        // Configure validator for RELAX NG Schema Support
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI, "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory");
        final SchemaFactory validationSchemaFactory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);

        final Source validationSchemaFile = new StreamSource(this.metadataSchemaSchema);
        final javax.xml.validation.Schema validationSchema = validationSchemaFactory.newSchema(validationSchemaFile);
        final Validator validator = validationSchema.newValidator();

        // Throws SAXException if the schema is not valid.
        // This needs to be a Stream Source, as validating a String isn't implemented :(
        validator.validate(new StreamSource(xmlFile));

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(new FileInputStream(xmlFile));

        // Schema is valid, continue the parse.
        final Schema schema = MetadataSchemaXMLReader.readXML(document);
        return schema;
    }
}
