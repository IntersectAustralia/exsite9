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
 * Tests {@link MetadataCategoryUse}
 */
public final class MetadataCategoryUseUnitTest
{
    @Test
    public void testMetadataCategoryUse()
    {
        final String[] strings = MetadataCategoryUse.asArray();
        assertEquals(3, strings.length);
        assertEquals("required", strings[0]);
        assertEquals("recommended", strings[1]);
        assertEquals("optional", strings[2]);
    }
}
