/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.util.Comparator;

import au.org.intersect.exsite9.domain.MetadataCategory;

/**
 * Comparator that can be used to sort {@link MetadataCategory}s by their ID.
 */
public final class IDMetadataCategoryComparator implements Comparator<MetadataCategory>
{
    /**
     * @{inheritDoc}
     */
    @Override
    public int compare(final MetadataCategory mdc1, final MetadataCategory mdc2)
    {
        if (mdc1.getId() < mdc2.getId())
        {
            return -1;
        }
        if (mdc1.getId() > mdc2.getId())
        {
            return 1;
        }
        return 0;
    }
}
