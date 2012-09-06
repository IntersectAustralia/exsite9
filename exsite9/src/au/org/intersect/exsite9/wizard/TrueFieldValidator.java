/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

/**
 * Validator that always returns true.
 */
public final class TrueFieldValidator implements IFieldValidator<String>
{
    @Override
    public String getErrorMessage()
    {
        return "";
    }

    @Override
    public String getWarningMessage()
    {
        return "";
    }

    @Override
    public boolean isValid(final String contents)
    {
        return true;
    }

    @Override
    public boolean warningExist(final String contents)
    {
        return false;
    }
}
