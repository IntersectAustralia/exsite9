/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.property;

import static org.junit.Assert.*;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Group;

/**
 * Tests {@link GroupNameTester}
 */
public final class GroupNameTesterUnitTest
{
    @Test
    public void testGroupNameTester()
    {
        final Group group = new Group("group name");
        final GroupNameTester gnt = new GroupNameTester();

        assertTrue(gnt.test(group, "", null, "group name"));
        assertFalse(gnt.test(group, "", null, "some other group name"));
    }
}
