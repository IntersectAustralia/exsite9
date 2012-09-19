/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.validators;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.util.DirectoryUtils;

/**
 * Validates Project Names.
 */
public final class ProjectNameValidator implements IFieldValidator<String>
{
    private String errorMessage;

    public ProjectNameValidator()
    {
    }

    @Override
    public String getErrorMessage()
    {
        return this.errorMessage;
    }

    @Override
    public String getWarningMessage()
    {
        return "";
    }

    @Override
    public boolean isValid(final String contents)
    {
        if (contents.trim().isEmpty())
        {
            this.errorMessage = "Project Name must not be empty.";
            return false;
        }

        if (contents.trim().length() >= 255)
        {
            this.errorMessage = "Project Name is too long.";
            return false;
        }

        if (!DirectoryUtils.isValidDirectoryName(contents.trim()))
        {
            this.errorMessage = "Project Name must contain alpha-numeric characters only";
            return false;
        }

        this.errorMessage = "";
        return true;
    }

    @Override
    public boolean warningExist(final String contents)
    {
        return false;
    }
}
