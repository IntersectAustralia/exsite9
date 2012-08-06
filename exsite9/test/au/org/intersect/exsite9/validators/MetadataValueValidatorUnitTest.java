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

import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * Tests {@link MetadataValueValidator}
 */
public final class MetadataValueValidatorUnitTest
{

    @Test
    public void testMetadataValueValidator()
    {
        final MetadataValue mv1 = new MetadataValue("mv1");
        final MetadataValueValidator toTest = new MetadataValueValidator(Arrays.asList(mv1));

        // Blank
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

        // name already used
        assertFalse(toTest.isValid(mv1.getValue()));
        assertFalse(toTest.getErrorMessage().isEmpty());

        assertTrue(toTest.isValid("newValue"));
        assertTrue(toTest.getErrorMessage().isEmpty());

        assertTrue(toTest.getWarningMessage().isEmpty());
        assertFalse(toTest.warningExist(""));
    }

    @Test
    public void testMetadataValueValidatorPermitBlanks()
    {
        final MetadataValue mv1 = new MetadataValue("mv1");
        final MetadataValueValidator toTest = new MetadataValueValidator(Arrays.asList(mv1), true);

        // Blank
        assertTrue(toTest.isValid(""));
        assertTrue(toTest.getErrorMessage().isEmpty());

        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= 255; i++)
        {
            sb.append("a");
        }

        // too long
        assertFalse(toTest.isValid(sb.toString()));
        assertFalse(toTest.getErrorMessage().isEmpty());

        // name already used
        assertFalse(toTest.isValid(mv1.getValue()));
        assertFalse(toTest.getErrorMessage().isEmpty());

        assertTrue(toTest.isValid("newValue"));
        assertTrue(toTest.getErrorMessage().isEmpty());

        assertTrue(toTest.getWarningMessage().isEmpty());
        assertFalse(toTest.warningExist(""));
    }
}
