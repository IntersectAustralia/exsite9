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
 * Comparator that can be used to sort {@link MetadataCategory}s alphabetically by their name.
 */
public final class AlphabeticalMetadataCategoryComparator implements Comparator<MetadataCategory>
{
    @Override
    public int compare(final MetadataCategory mdc1, final MetadataCategory mdc2)
    {
        final String name1 = mdc1.getName();
        final String name2 = mdc2.getName();

        return name1.compareToIgnoreCase(name2);
    }
}
