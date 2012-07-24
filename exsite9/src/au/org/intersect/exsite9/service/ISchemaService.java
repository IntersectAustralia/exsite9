/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Schema;

/**
 * Provides services for manipulating Metadata {@link Schema}s
 */
public interface ISchemaService
{
    /**
     * Creates a new Local Schema
     * @param schemaName The name of the schema.
     * @param schemaDescription The description of the schema.
     * @param schemaNamespaceURL The namespace URL of the schema.
     * @return The newly created schema.
     */
    public Schema createLocalSchema(final String schemaName, final String schemaDescription, final String schemaNamespaceURL);

    /**
     * Persists an Imported Schema
     * @param schema The schema to persist.
     */
    public void createImportedSchema(final Schema schema);

    /**
     * Updates the details of the provided schema.
     * @param schema The schema to update.
     * @param schemaName The new name of the schema.
     * @param schemaDescription The new description of the schema.
     * @param schemaNamespaceURL The new namespace URL of the schema.
     */
    public void updateSchema(final Schema schema, final String schemaName, final String schemaDescription, final String schemaNamespaceURL);

    /**
     * Removes a schema, its association to a project, and its association to files/groups.
     * @param schema The schema to delete.
     */
    public void removeSchema(final Schema schema);

    /**
     * Adds a metadata category to a schema.
     * @param schema The schema.
     * @param metadataCategory The metadata category to add.
     */
    public void addMetadataCategoryToSchema(final Schema schema, final MetadataCategory metadataCategory);

    /**
     * Obtains the default directory on the file system that contains the pre-defined Schemas.
     * @return The default directory on the file system that contains the pre-defined Schemas. May be {@code null} if there is no default schema directory.
     */
    public File getDefaultSchemaDirectory();

    /**
     * Parses the provides schema XML file to a schema object. <em>Does not persist the Schema to the database.</em>
     * @param xmlFile The XML File to import.
     * @return The imported Schema.
     */
    public Schema parseSchema(final File xmlFile) throws SAXException, IOException, ParserConfigurationException;
}
