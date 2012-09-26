/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryViewConfiguration;

/**
 * Service to manipulate {@link MetadataCategoryViewConfiguration items}.
 */
public interface IMetadataCategoryViewConfigService
{
    /**
     * Determines if the metadata category should be displayed as being expanded.
     * @param metadataCategory The meteadata category.
     * @return {@code true} if it should be expanded. {@code false} otherwise.
     */
    boolean isExpanded(final MetadataCategory metadataCategory);

    /**
     * Sets wether or not a metadata category should be displayed as being expanded.
     * @param metadataCategory The metadata category.
     * @param expanded {@code true} if it should be expanded. {@code false} otherwise.
     */
    void setExpanded(final MetadataCategory metadataCategory, final boolean expanded);
}
