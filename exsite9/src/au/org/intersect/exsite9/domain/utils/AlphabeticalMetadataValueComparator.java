/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.util.Comparator;

import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * Comparator that can be used to sort {@link MetadataValue}s alphabetically.
 */
public final class AlphabeticalMetadataValueComparator implements Comparator<MetadataValue>
{
    /**
     * @{inheritDoc}
     */
    @Override
    public int compare(final MetadataValue mdv1, final MetadataValue mdv2)
    {
        final String value1 = mdv1.getValue();
        final String value2 = mdv2.getValue();

        return value1.compareTo(value2);
    }
}