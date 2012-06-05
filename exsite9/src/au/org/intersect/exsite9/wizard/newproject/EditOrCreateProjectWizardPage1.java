/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newproject;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * The first page of the new project wizard.
 */
public final class EditOrCreateProjectWizardPage1 extends WizardPage implements KeyListener
{
    private ValidatingField<String> projectNameField;

    private Text projectDescriptionText;
    private Text projectOwnerText;

    private String projectNameFieldTextDefaultValue;
    private String projectOwnerFieldTextDefaultValue;
    private String projectDescriptionFieldTextDefaultValue;

    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private Composite container;

    /**
     * Constructor
     */
    public EditOrCreateProjectWizardPage1(String pageTitle, String pageDescription,
            String projectNameFieldTextDefaultValue, String projectOwnerFieldTextDefaultValue,
            String projectDescriptionFieldTextDefaultValue)
    {
        super(pageTitle);
        setTitle(pageTitle);
        setDescription(pageDescription);
        this.projectNameFieldTextDefaultValue = projectNameFieldTextDefaultValue;
        this.projectOwnerFieldTextDefaultValue = projectOwnerFieldTextDefaultValue;
        this.projectDescriptionFieldTextDefaultValue = projectDescriptionFieldTextDefaultValue;
    }

    /**
     * @{inheritDoc
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

        final Label projectNameLabel = new Label(this.container, SWT.NULL);
        projectNameLabel.setText("Project Name");

        projectNameField = this.stringValidatorToolkit.createTextField(this.container, new IFieldValidator<String>()
        {
            @Override
            public boolean warningExist(final String conents)
            {
                return false;
            }

            @Override
            public boolean isValid(final String contents)
            {
                return !(contents.trim().isEmpty());
            }

            @Override
            public String getWarningMessage()
            {
                return "";
            }

            @Override
            public String getErrorMessage()
            {
                return "Project name must not be empty.";
            }
        }, true, projectNameFieldTextDefaultValue);

        this.projectNameField.getControl().addKeyListener(this);

        final Label projectOwnerLabel = new Label(this.container, SWT.NULL);
        projectOwnerLabel.setText("Project Owner");

        this.projectOwnerText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectOwnerText.setText(projectOwnerFieldTextDefaultValue);
        this.projectOwnerText.addKeyListener(this);

        final Label projectDescriptionLabel = new Label(this.container, SWT.NULL);
        projectDescriptionLabel.setText("Project Description");

        this.projectDescriptionText = new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        this.projectDescriptionText.setText(projectDescriptionFieldTextDefaultValue);
        this.projectDescriptionText.addKeyListener(this);

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.projectNameField.getControl().setLayoutData(singleLineGridData);
        this.projectOwnerText.setLayoutData(singleLineGridData);
        this.projectDescriptionText.setLayoutData(multiLineGridData);

        setControl(this.container);
        setPageComplete(false);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void keyPressed(final KeyEvent e)
    {
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void keyReleased(final KeyEvent e)
    {
        setPageComplete(this.projectNameField.isValid());
    }

    public String getProjectName()
    {
        return this.projectNameField.getContents().trim();
    }

    public String getProjectOwner()
    {
        return this.projectOwnerText.getText().trim();
    }

    public String getProjectDescription()
    {
        return this.projectDescriptionText.getText().trim();
    }
}
