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
 * Tests {@link MetadataCategoryType}
 */
public final class MetadataCategoryTypeUnitTest
{

    @Test
    public void testMetadataCategoryType()
    {
        assertEquals("Controlled Vocabulary", MetadataCategoryType.CONTROLLED_VOCABULARY.toString());
        assertEquals("Free Text", MetadataCategoryType.FREETEXT.toString());

        final String[] strings = MetadataCategoryType.toArray();
        assertEquals(2, strings.length);
        assertEquals("Controlled Vocabulary", strings[0]);
        assertEquals("Free Text", strings[1]);

        assertEquals(MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryType.fromString("Controlled Vocabulary"));
        assertEquals(MetadataCategoryType.FREETEXT, MetadataCategoryType.fromString("Free Text"));
        assertNull(MetadataCategoryType.fromString(""));
    }
}
