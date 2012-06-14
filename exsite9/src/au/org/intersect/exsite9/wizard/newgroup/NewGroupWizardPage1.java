/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newgroup;

import java.util.List;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * The wizard page to display when adding a new group.
 */
public final class NewGroupWizardPage1 extends WizardPage implements KeyListener
{
    private Composite container;

    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private ValidatingField<String> groupNameField;

    private final Group parentGroup;
    
    public NewGroupWizardPage1(final Group parentGroup)
    {
        super("New Group");
        setTitle("New Group");
        setDescription("Please enter the details of your new group");
        this.parentGroup = parentGroup;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;

        this.stringValidatorToolkit = new StringValidationToolkit(SWT.TOP | SWT.LEFT, 1, true);
        this.stringValidatorToolkit.setDefaultErrorMessageHandler(this.errorMessageHandler);

        final Label groupNameLabel = new Label(this.container, SWT.NULL);
        groupNameLabel.setText("Group Name");

        groupNameField = this.stringValidatorToolkit.createTextField(this.container, new IFieldValidator<String>()
        {
            private String errorMessage;

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
                    this.errorMessage = "Group name must not be empty.";
                    return false;
                }

                if (contents.trim().length() >= 255)
                {
                    this.errorMessage = "Group name is too long.";
                    return false;
                }

                // Check if there is a child group with this name already.
                final List<Group> existingGroups = parentGroup.getGroups();
                for (final Group existingGroup : existingGroups)
                {
                    if (existingGroup.getName().equalsIgnoreCase(contents.trim()))
                    {
                        this.errorMessage = "A Group with that name already exists.";
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
        }, true, "");

        this.groupNameField.getControl().addKeyListener(this);

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        this.groupNameField.getControl().setLayoutData(singleLineGridData);

        setControl(this.container);
        setPageComplete(false);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void keyPressed(final KeyEvent e)
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent e)
    {
        setPageComplete(this.groupNameField.isValid());
    }

    public String getNewGroupName()
    {
        return this.groupNameField.getContents().trim();
    }
}
