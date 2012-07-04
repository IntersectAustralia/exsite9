package au.org.intersect.exsite9.wizard;

import org.eclipse.swt.widgets.Composite;

import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.ValidationToolkit;

public class WizardFieldUtils
{

    public static ValidatingField<String> createOptional255TextField(ValidationToolkit<String> toolkit, Composite composite,
            String fieldName, String defaultValue)
    {
        return toolkit.createTextField(composite, new MaximumFieldLengthValidator(fieldName, 255), false, defaultValue);
    }
}
