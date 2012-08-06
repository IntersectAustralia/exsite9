/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.validators;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;

/**
 * Tests {@link MetadataCategoryNameValidator}
 */
public final class MetadataCategoryNameValidatorUnitTest
{
    @Test
    public void testValidator()
    {
        final MetadataCategory mc1 = new MetadataCategory("MC1", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.recommended);
        final MetadataCategory mc2 = new MetadataCategory("MC2", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.recommended);

        final MetadataCategoryNameValidator toTest = new MetadataCategoryNameValidator(Arrays.asList(mc1, mc2), mc2);

        // is empty
        assertFalse(toTest.isValid(""));
        assertFalse(toTest.getErrorMessage().isEmpty());

        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= 255; i++)
        {
            sb.append("a");
        }

        // too long
        assertFalse(toTest.isValid(sb.toString()));
        assertFalse(toTest.getErrorMessage().isEmpty());

        // invalid XML
        assertFalse(toTest.isValid("\"\""));
        assertFalse(toTest.getErrorMessage().isEmpty());

        // invalid XML
        assertFalse(toTest.isValid("new metadata category name"));
        assertFalse(toTest.getErrorMessage().isEmpty());

        // name already used.
        assertFalse(toTest.isValid(mc1.getName()));
        assertFalse(toTest.getErrorMessage().isEmpty());

        // valid, since mc2 is excluded
        assertTrue(toTest.isValid(mc2.getName()));
        assertTrue(toTest.getErrorMessage().isEmpty());

        assertFalse(toTest.warningExist("blah"));
        assertTrue(toTest.getWarningMessage().isEmpty());
    }
}
