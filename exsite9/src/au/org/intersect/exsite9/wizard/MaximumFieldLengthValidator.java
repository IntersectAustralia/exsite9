/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id: intersect_codetemplates.xml 29 2010-07-16 05:45:06Z georgina $
 */
package au.org.intersect.exsite9.wizard;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

/**
 *
 * @version $Rev: 29 $
 */
public final class MaximumFieldLengthValidator implements IFieldValidator<String>
{
    private final String fieldName;
    private final int maxLength;

    public MaximumFieldLengthValidator(final String fieldName ,final int maxLength)
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
    public boolean isValid(String contents)
    {
        return contents.trim().length() < maxLength;
    }

    @Override
    public boolean warningExist(String contents)
    {
        return false;
    }

}
