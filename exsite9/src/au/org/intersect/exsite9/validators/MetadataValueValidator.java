/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.validators;

import java.util.List;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * Validator used to validate a Metadata Value.
 */
public final class MetadataValueValidator implements IFieldValidator<String>
{
    private String errorMessage;

    private final List<MetadataValue> existingMetadataValues;
    private final boolean permitBlank;

    public MetadataValueValidator(final List<MetadataValue> existingMetadataValues)
    {
        this(existingMetadataValues, false);
    }

    public MetadataValueValidator(final List<MetadataValue> existingMetadataValues, final boolean permitBlank)
    {
        this.existingMetadataValues = existingMetadataValues;
        this.permitBlank = permitBlank;
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
        if (contents.trim().isEmpty() && !permitBlank)
        {
            this.errorMessage = "Value must not be empty.";
            return false;
        }

        if (contents.trim().length() >= 255)
        {
            this.errorMessage = "Value is too long.";
            return false;
        }

        for (final MetadataValue existingValue : this.existingMetadataValues)
        {
            if (existingValue.getValue().equalsIgnoreCase(contents.trim()))
            {
                this.errorMessage = "A Value with that name already exists for this Category.";
                return false;
            }
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
