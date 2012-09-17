/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.util.Comparator;

import au.org.intersect.exsite9.domain.MetadataAttributeValue;

/**
 * Comparator used to sort {@link MetadataAttributeValue}s alphabetically.
 */
public final class AlphabeticalMetadataAttributeValueComparator implements Comparator<MetadataAttributeValue>
{

    /**
     * @{inheritDoc}
     */
    @Override
    public int compare(final MetadataAttributeValue mav1, final MetadataAttributeValue mav2)
    {
        final String val1 = mav1.getValue();
        final String val2 = mav2.getValue();
        return val1.compareToIgnoreCase(val2);
    }
}
