/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.createsubmissionpackage;

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

import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * First page of the 'Create Submission Package' Wizard.
 * Allows configuration of a name and description.
 */
public final class CreateSubmissionPackageWizardPage1 extends WizardPage implements KeyListener
{
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private StringValidationToolkit stringValidatorToolkit;

    private Composite container;

    private ValidatingField<String> nameField;
    private Text descriptionField;

    private SubmissionPackage currentSubmissionPackage;

    public CreateSubmissionPackageWizardPage1(final SubmissionPackage submissionPackage)
    {
        super("Create Submission Package");
        setTitle("Create Submission Package");
        setDescription("Configure properties of the submission package");
        this.currentSubmissionPackage = submissionPackage;
    }


    @Override
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;

        this.stringValidatorToolkit = new StringValidationToolkit(SWT.TOP | SWT.LEFT, 1, true);
        this.stringValidatorToolkit.setDefaultErrorMessageHandler(this.errorMessageHandler);

        final Label submissionPackageNameLabel = new Label(this.container, SWT.NULL);
        submissionPackageNameLabel.setText("Name");

        this.nameField = this.stringValidatorToolkit.createTextField(this.container, new IFieldValidator<String>()
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
                return "Submission package name must not be empty.";
            }
        }, true, this.currentSubmissionPackage == null ? "" : this.currentSubmissionPackage.getName());

        this.nameField.getControl().addKeyListener(this);

        final Label descriptionLabel = new Label(this.container, SWT.NULL);
        descriptionLabel.setText("Description");

        this.descriptionField = new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        this.descriptionField.setText(this.currentSubmissionPackage == null ? "" : this.currentSubmissionPackage.getDescription());

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.nameField.getControl().setLayoutData(singleLineGridData);
        this.descriptionField.setLayoutData(multiLineGridData);

        setControl(this.container);
        setPageComplete(this.currentSubmissionPackage != null);
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
        setPageComplete(this.nameField.isValid());
    }

    public String getSubmissionPackageName()
    {
        return this.nameField.getContents().trim();
    }

    public String getSubmissionPackageDescription()
    {
        return this.descriptionField.getText().trim();
    }
}
