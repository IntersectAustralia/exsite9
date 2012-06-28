/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.util.Pair;

/**
 * Tests {@link MetadataAssignableUtils}
 */
public final class GroupUtilsUnitTest
{
    @Test
    public void testGetCategoryToValueMapping()
    {
        final Group group = new Group("group");

        final MetadataCategory cat1 = new MetadataCategory("cat1");
        cat1.setId(12l);
        final MetadataCategory cat2 = new MetadataCategory("cat2");
        cat2.setId(32l);

        final MetadataValue val1 = new MetadataValue("val1");
        final MetadataValue val2 = new MetadataValue("val2");

        final MetadataAssociation mda1 = new MetadataAssociation(cat1);
        mda1.setId(1l);
        mda1.getMetadataValues().add(val1);

        final MetadataAssociation mda2 = new MetadataAssociation(cat2);
        mda2.setId(2l);
        mda2.getMetadataValues().add(val2);

        group.getMetadataAssociations().add(mda1);
        group.getMetadataAssociations().add(mda2);

        final Set<Pair<MetadataCategory, MetadataValue>> out = MetadataAssignableUtils.getCategoryToValueMapping(group);
        assertNotNull(out);
        assertEquals(2, out.size());

        assertTrue(out.contains(new Pair<MetadataCategory, MetadataValue>(cat1, val1)));
        assertTrue(out.contains(new Pair<MetadataCategory, MetadataValue>(cat2, val2)));
    }
}
