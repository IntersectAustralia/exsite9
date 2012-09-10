/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.validators;

import java.util.List;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.domain.MetadataAttributeValue;

/**
 * Validator used to validate a Metadata Attribute Value.
 */
public final class MetadataAttributeValueValidator implements IFieldValidator<String>
{
    private String errorMessage;

    private final List<MetadataAttributeValue> existingMetadataValues;

    public MetadataAttributeValueValidator(final List<MetadataAttributeValue> existingMetadataValues)
    {
        this.existingMetadataValues = existingMetadataValues;
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
            this.errorMessage = "Value must not be empty.";
            return false;
        }

        for (final MetadataAttributeValue existingValue : this.existingMetadataValues)
        {
            if (existingValue.getValue().equalsIgnoreCase(contents.trim()))
            {
                this.errorMessage = "A Value with that name already exists.";
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
