/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.io.File;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Schema;

/**
 * Provides services for manipulating {@link Schema}s
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
}
