/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.Schema;

/**
 * Provides services for manipulating {@link Schema}s
 */
public interface ISchemaService
{
    public void addMetadataCategoryToSchema(final Schema schema, final MetadataCategory metadataCategory);
}
