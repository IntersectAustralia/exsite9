package au.org.intersect.exsite9.validators;

import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.xml.XMLUtils;

/**
 * Validates Metadata Attribute Names.
 */
public final class MetadataAttributeNameValidator implements IFieldValidator<String>
{
    private String errorMessage;

    public MetadataAttributeNameValidator()
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
        if (contents.isEmpty())
        {
            return true;
        }

        if (contents.trim().length() >= 255)
        {
            this.errorMessage = "Metadata Attribute name is too long.";
            return false;
        }

        if (!XMLUtils.isValidElementOrAttribute(contents.trim()))
        {
            this.errorMessage = "Metadata Attribtue name is not a valid XML element.";
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
