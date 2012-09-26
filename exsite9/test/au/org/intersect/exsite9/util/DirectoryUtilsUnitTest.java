/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link DirectoryUtils}
 */
public final class DirectoryUtilsUnitTest
{
    @Test
    public void testIsValidDirectoryName()
    {
        assertTrue(DirectoryUtils.isValidDirectoryName("some directory name with a space"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name \\ with a bad char"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name / with a bad char"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name : with a bad char"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name * with a bad char"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name \" with a bad char"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name ? with a bad char"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name < with a bad char"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name > with a bad char"));
        assertFalse(DirectoryUtils.isValidDirectoryName("some directory name | with a bad char"));
    }
}
