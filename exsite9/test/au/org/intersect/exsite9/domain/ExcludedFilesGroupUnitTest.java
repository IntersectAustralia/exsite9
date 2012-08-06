/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link ExcludedFilesGroup}
 */
public final class ExcludedFilesGroupUnitTest
{
    @Test
    public void testExcludedFilesGroup()
    {
        final ExcludedFilesGroup efg = new ExcludedFilesGroup();
        assertTrue(efg instanceof Group);
        assertEquals("Excluded Files", efg.getName());

        try
        {
            efg.getGroups().add(new Group("some group"));
            fail();
        }
        catch (final UnsupportedOperationException e)
        {
        }

        try
        {
            efg.getMetadataAssociations().add(new MetadataAssociation());
            fail();
        }
        catch (final UnsupportedOperationException e)
        {
        }
    }
}
