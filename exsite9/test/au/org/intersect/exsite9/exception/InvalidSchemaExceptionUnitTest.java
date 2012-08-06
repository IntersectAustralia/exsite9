/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.exception;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link InvalidSchemaException}
 */
public final class InvalidSchemaExceptionUnitTest
{
    @Test
    public void testInvaludSchemaException()
    {
        final InvalidSchemaException toTest = new InvalidSchemaException("some reason");
        assertEquals("some reason", toTest.getMessage());
        assertTrue(toTest instanceof Exception);
    }
}
