/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

/**
 * Validator to enforce a maximum length of string fields.
 */
public final class MaximumFieldLengthValidator implements IFieldValidator<String>
{
    private final String fieldName;
    private final int maxLength;

    /**
     * @param fieldName The name of the field, used in forming error messages.
     * @param maxLength The maximum length of the field.
     */
    public MaximumFieldLengthValidator(final String fieldName, final int maxLength)
    {
        this.fieldName = fieldName;
        this.maxLength = maxLength;
    }

    @Override
    public String getErrorMessage()
    {
        return fieldName + " must be less than " + maxLength + " characters in length";
    }

    @Override
    public String getWarningMessage()
    {
        return "";
    }

    @Override
    public boolean isValid(final String contents)
    {
        return contents.trim().length() < maxLength;
    }

    @Override
    public boolean warningExist(final String contents)
    {
        return false;
    }

}
