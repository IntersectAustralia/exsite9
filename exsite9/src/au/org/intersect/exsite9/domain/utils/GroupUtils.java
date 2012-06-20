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

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.util.Pair;

/**
 * Utilities for {@link Group}
 */
public final class GroupUtils
{
    /**
     * Obtains the set of metadata category to metadata value mappings for the provided group by iterating through its metadata associations.
     * @param group The group.
     * @return the set of metadata category to metadata value mappings.
     */
    public static Set<Pair<MetadataCategory, MetadataValue>> getCategoryToValueMapping(final Group group)
    {
        final Set<Pair<MetadataCategory, MetadataValue>> toReturn = new HashSet<Pair<MetadataCategory,MetadataValue>>();

        final List<MetadataAssociation> metadataAssociations = group.getMetadataAssociations();
        for (final MetadataAssociation metadataAssociation : metadataAssociations)
        {
            final MetadataCategory metadataCategory = metadataAssociation.getMetadataCategory();
            for (final MetadataValue metadataValue : metadataAssociation.getMetadataValues())
            {
                toReturn.add(new Pair<MetadataCategory, MetadataValue>(metadataCategory, metadataValue));
            }
        }
        return toReturn;
    }
}
