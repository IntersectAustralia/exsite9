/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * For performing operations with {@link MetadataCategory}
 */
public interface IMetadataCategoryService
{    
    void updateMetadataCategory(MetadataCategory existingMetadataCategoryToUpdate, final String name, final MetadataCategoryUse use, final boolean inExtensible, final List<MetadataValue> values);
    
    MetadataCategory findById(final Long id);

    void deleteMetadataCategory(final MetadataCategory metadataCategory);

    MetadataValue addValueToMetadataCategory(final MetadataCategory metadataCategory, final String metadataValue);

    /**
     * Creates a new metadata category.
     * @param name The name of the metadata category.
     * @param type The type of the metadata category.
     * @param use The use of the metadata category.
     * @param inextensible {@code true} if this metadata category is inextensible. {@code false} otherwiss.
     * @param values The allowable values for this metadata category.
     * @param metadataAttribute The attribute for this metadata category. May be {@code null}.
     * @return The newly created metadata category.
     */
    MetadataCategory createNewMetadataCategory(final String name, final MetadataCategoryType type, final MetadataCategoryUse use,
            final boolean inextensible, final List<MetadataValue> values, final MetadataAttribute metadataAttribute);
}
