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
 * Tests {@link RootGroup}
 */
public final class RootGroupUnitTest
{
    @Test
    public void testRootGroup()
    {
        final RootGroup toTest = new RootGroup("some name");
        assertTrue(toTest instanceof Group);

        try
        {
            toTest.getMetadataAssociations().add(new MetadataAssociation());
            fail();
        }
        catch (final UnsupportedOperationException e)
        {
        }
    }
}
