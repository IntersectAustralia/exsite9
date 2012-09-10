/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * For performing operations with {@link MetadataCategory}
 */
public interface IMetadataCategoryService
{    
    void updateMetadataCategory(MetadataCategory existingMetadataCategoryToUpdate, final String name, final String description, final MetadataCategoryUse use, final boolean inExtensible, final List<MetadataValue> values);
    
    MetadataCategory findById(final Long id);

    void deleteMetadataCategory(final MetadataCategory metadataCategory);

    MetadataValue addValueToMetadataCategory(final MetadataCategory metadataCategory, final String metadataValue);

    MetadataCategory createNewMetadataCategory(String name, String description, MetadataCategoryType type, MetadataCategoryUse use,
            boolean inextensible, boolean imported, List<MetadataValue> values);
}
