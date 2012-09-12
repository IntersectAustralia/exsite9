/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests {@link NotEnoughSpaceForZIPException}
 */
public final class NotEnoughSpaceForZIPExceptionUnitTest
{
    @Test
    public void test()
    {
        final NotEnoughSpaceForZIPException toTest = new NotEnoughSpaceForZIPException("some reason");
        assertEquals("some reason", toTest.getMessage());
        assertTrue(toTest instanceof Exception);
    }
}
