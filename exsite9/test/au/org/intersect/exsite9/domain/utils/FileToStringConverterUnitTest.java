/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

/**
 * Tests {@link FileToStringConverter}
 */
public final class FileToStringConverterUnitTest
{

    @Test
    public void testFileToStringConverterUnitTest()
    {
        final FileToStringConverter toTest = new FileToStringConverter();
        final String userHomeDir = System.getProperty("user.home");
        final File file = new File(userHomeDir, "someNewFile");

        assertEquals(file.getAbsolutePath(), toTest.convertObjectValueToDataValue(file, null));
        assertEquals(file, toTest.convertDataValueToObjectValue(file.getAbsolutePath(), null));

        try
        {
            toTest.convertDataValueToObjectValue(new Object(), null);
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            // expected.
        }

        try
        {
            toTest.convertObjectValueToDataValue(new Object(), null);
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            // expected.
        }

        assertFalse(toTest.isMutable());

        // does nothing
        toTest.initialize(null, null);
    }
}
