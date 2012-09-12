/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import au.org.intersect.exsite9.domain.MetadataAttributeValue;

/**
 * Tests {@link MetadataAttributeValueValidator}
 */
public final class MetadataAttributeValueValidatorUnitTest
{
    @Test
    public void test()
    {
        final MetadataAttributeValue mv1 = new MetadataAttributeValue("mv1");
        final MetadataAttributeValueValidator toTest = new MetadataAttributeValueValidator(Arrays.asList(mv1));

        // Blank
        assertFalse(toTest.isValid(""));
        assertFalse(toTest.getErrorMessage().isEmpty());

        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= 255; i++)
        {
            sb.append("a");
        }

        assertTrue(toTest.isValid(sb.toString()));
        assertTrue(toTest.getErrorMessage().isEmpty());

        // name already used
        assertFalse(toTest.isValid(mv1.getValue()));
        assertFalse(toTest.getErrorMessage().isEmpty());

        assertTrue(toTest.isValid("newValue"));
        assertTrue(toTest.getErrorMessage().isEmpty());

        assertTrue(toTest.getWarningMessage().isEmpty());
        assertFalse(toTest.warningExist(""));
    }
}
