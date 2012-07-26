/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.validators;

import java.util.List;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.xml.XMLUtils;

/**
 * A Validator used to validate a Category Name.
 */
public final class MetadataCategoryNameValidator implements IFieldValidator<String>
{
    private String errorMessage;

    private final List<MetadataCategory> existingCategories;
    private final MetadataCategory currentMetadataCategory;

    /**
     * Constructor
     * @param existingCategories The existing categories - used to ensure there are no duplicate names.
     * @param currrent The "current" metadata category being validated. May be {@code null}, but used when we wish to exclude clash validation on one metadata category.
     */
    public MetadataCategoryNameValidator(final List<MetadataCategory> existingCategories, final MetadataCategory currrent)
    {
        this.existingCategories = existingCategories;
        this.currentMetadataCategory = currrent;
    }

    @Override
    public boolean warningExist(final String conents)
    {
        return false;
    }

    @Override
    public boolean isValid(final String contents)
    {
        if (contents.trim().isEmpty())
        {
            this.errorMessage = "Category name must not be empty.";
            return false;
        }

        if (contents.trim().length() >= 255)
        {
            this.errorMessage = "Category name is too long.";
            return false;
        }

        if (!XMLUtils.isValidElementName(contents.trim()))
        {
            this.errorMessage = "Category name is not a valid XML element.";
            return false;
        }
        
        for (final MetadataCategory existingCategory : this.existingCategories)
        {
            final String currentMetadataCategoryName = currentMetadataCategory == null ? "" : currentMetadataCategory.getName();
            if (existingCategory.getName().equalsIgnoreCase(contents.trim()) && !contents.trim().equals(currentMetadataCategoryName))
            {
                this.errorMessage = "A category with that name already exists.";
                return false;
            }
        }
        return true;
    }

    @Override
    public String getWarningMessage()
    {
        return "";
    }

    @Override
    public String getErrorMessage()
    {
        return this.errorMessage;
    }
}
