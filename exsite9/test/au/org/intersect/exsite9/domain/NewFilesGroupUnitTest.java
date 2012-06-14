/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

/**
 * Tests {@link NewFilesGroup}
 */
public final class NewFilesGroupUnitTest
{

    @Test
    public void testConstruction()
    {
        final NewFilesGroup toTest = new NewFilesGroup();
        assertTrue(toTest instanceof Group);
        assertEquals("New Files", toTest.getName());
        final List<Group> childGroups = toTest.getGroups();
        assertTrue(childGroups.isEmpty());

        try
        {
            childGroups.add(new NewFilesGroup());
            fail();
        }
        catch (final UnsupportedOperationException e)
        {
            // expected.
        }
    }
}
