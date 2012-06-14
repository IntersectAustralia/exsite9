/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.MetadataCategory;

/**
 * For performing operations with {@link MetadataCategory}
 */
public interface IMetadataCategoryService
{
    MetadataCategory createNewMetadataCategory(final String name, final List<String> values);
}
