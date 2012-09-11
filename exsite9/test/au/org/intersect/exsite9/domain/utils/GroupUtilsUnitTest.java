/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.util.Triplet;

/**
 * Tests {@link MetadataAssignableUtils}
 */
public final class GroupUtilsUnitTest
{
    @Test
    public void testGetCategoryToValueMapping()
    {
        final Group group = new Group("group");

        final MetadataCategory cat1 = new MetadataCategory("cat1", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        cat1.setId(12l);
        final MetadataCategory cat2 = new MetadataCategory("cat2", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        cat2.setId(32l);

        final MetadataValue val1 = new MetadataValue("val1");
        final MetadataValue val2 = new MetadataValue("val2");

        final MetadataAttributeValue mdav1 = new MetadataAttributeValue("mdav1");
        final MetadataAttributeValue mdav2 = new MetadataAttributeValue("mdav1");
        mdav1.setId(1l);
        mdav2.setId(2l);

        final MetadataAssociation mda1 = new MetadataAssociation(cat1);
        mda1.setId(1l);
        mda1.getMetadataValues().add(val1);
        mda1.setMetadataAttributeValue(mdav1);

        final MetadataAssociation mda2 = new MetadataAssociation(cat2);
        mda2.setId(2l);
        mda2.getMetadataValues().add(val2);
        mda2.setMetadataAttributeValue(mdav2);

        group.getMetadataAssociations().add(mda1);
        group.getMetadataAssociations().add(mda2);

        final Set<Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>> out = MetadataAssignableUtils.getCategoryToValueMapping(group);
        assertNotNull(out);
        assertEquals(2, out.size());

        assertTrue(out.contains(new Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>(cat1, val1, mdav1)));
        assertTrue(out.contains(new Triplet<MetadataCategory, MetadataValue, MetadataAttributeValue>(cat2, val2, mdav2)));
    }
}
