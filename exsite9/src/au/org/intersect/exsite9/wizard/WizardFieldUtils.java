package au.org.intersect.exsite9.wizard;

import org.eclipse.swt.widgets.Composite;

import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.ValidationToolkit;

/**
 * Utilities for fields in Wizards.
 */
public final class WizardFieldUtils
{

    /**
     * Creates a text field configured to be optional, with a maximum length of 255 chars.
     * @param toolkit The toolkit.
     * @param composite The composite.
     * @param fieldName The name of the field.
     * @param defaultValue The default value.
     * @return The field.
     */
    public static ValidatingField<String> createOptional255TextField(final ValidationToolkit<String> toolkit, final Composite composite,
            final String fieldName, final String defaultValue)
    {
        return toolkit.createTextField(composite, new MaximumFieldLengthValidator(fieldName, 255), false, defaultValue);
    }
}
