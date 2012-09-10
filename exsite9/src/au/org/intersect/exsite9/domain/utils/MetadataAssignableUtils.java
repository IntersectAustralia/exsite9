/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.org.intersect.exsite9.domain.IMetadataAssignable;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.util.Triplet;

/**
 * Utilities for {@link IMetadataAssignable}
 */
public final class MetadataAssignableUtils
{
    private MetadataAssignableUtils()
    {
        // No instances please.
    }

    /**
     * Obtains the set of metadata category to metadata value mappings for the provided group by iterating through its metadata associations.
     * @param group The group.
     * @return the set of metadata category to metadata value mappings.
     */
    public static Set<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>> getCategoryToValueMapping(final IMetadataAssignable metadataAssignable)
    {
        final Set<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>> toReturn = new HashSet<Triplet<MetadataCategory,MetadataValue, MetadataAttributeValue>>();

        final List<MetadataAssociation> metadataAssociations = metadataAssignable.getMetadataAssociations();
        for (final MetadataAssociation metadataAssociation : metadataAssociations)
        {
            final MetadataCategory metadataCategory = metadataAssociation.getMetadataCategory();
            final MetadataAttributeValue metadataAttributeValue = metadataAssociation.getMetadataAttributeValue();
            for (final MetadataValue metadataValue : metadataAssociation.getMetadataValues())
            {
                toReturn.add(new Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>(metadataCategory, metadataValue, metadataAttributeValue));
            }
        }
        return toReturn;
    }
}
