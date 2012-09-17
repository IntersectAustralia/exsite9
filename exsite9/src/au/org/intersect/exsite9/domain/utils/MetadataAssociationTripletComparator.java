/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import java.util.Comparator;

import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.util.Triplet;

/**
 * Comparator that can be used to sort MetadataAssociation Triplets
 */
public final class MetadataAssociationTripletComparator implements Comparator<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>>
{

    @Override
    public int compare(final Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue> t1, final Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue> t2)
    {
        final long mcId1 = t1.getFirst().getId();
        final long mcId2 = t2.getFirst().getId();

        if (mcId1 < mcId2)
        {
            return -1;
        }
        if (mcId1 > mcId2)
        {
            return 1;
        }

        final long mvId1 = t1.getSecond().getId();
        final long mvId2 = t2.getSecond().getId();

        if (mvId1 < mvId2)
        {
            return -1;
        }
        if (mvId1 > mvId2)
        {
            return 1;
        }

        // MetadataAttributeValues can be null.
        if (t1.getThird() == null && t2.getThird() == null)
        {
            return 0;
        }
        if (t1.getThird() == null)
        {
            return 1;
        }
        if (t2.getThird() == null)
        {
            return -1;
        }
        
        final long mavId1 = t1.getThird().getId();
        final long mavId2 = t2.getThird().getId();

        if (mavId1 < mavId2)
        {
            return -1;
        }
        if (mavId1 > mavId2)
        {
            return 1;
        }

        return 0;
    }
}
