/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.validators;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

/**
 * Validator that always returns true.
 */
public final class NonEmptyValidator implements IFieldValidator<String>
{
    private final String fieldName;

    public NonEmptyValidator(final String fieldName)
    {
        this.fieldName = fieldName;
    }

    @Override
    public String getErrorMessage()
    {
        return this.fieldName + " must not be empty";
    }

    @Override
    public String getWarningMessage()
    {
        return "";
    }

    @Override
    public boolean isValid(final String contents)
    {
        return !contents.trim().isEmpty();
    }

    @Override
    public boolean warningExist(final String contents)
    {
        return false;
    }
}
