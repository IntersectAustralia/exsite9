/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.createsubmissionpackage;

import java.util.Collection;

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
import au.org.intersect.exsite9.wizard.MaximumFieldLengthValidator;
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
    private ValidatingField<String> descriptionField;

    private SubmissionPackage currentSubmissionPackage;
    private final Collection<SubmissionPackage> existingSubmissionPackages;

    public CreateSubmissionPackageWizardPage1(final SubmissionPackage submissionPackage, final Collection<SubmissionPackage> existingSubmissionPackages, final String pageTitle)
    {
        super(pageTitle);
        setTitle(pageTitle);
        setDescription("Configure properties of the submission package");
        this.currentSubmissionPackage = submissionPackage;
        this.existingSubmissionPackages = existingSubmissionPackages;
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
            private String errorMessage = "";

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
                    this.errorMessage = "Submission package name must not be empty";
                    return false;
                }
                if (contents.trim().length() >= 255)
                {
                    this.errorMessage = "Submission package name must be less than 255 characters in length";
                    return false;
                }
                for (final SubmissionPackage existingSubmissionPackage : existingSubmissionPackages)
                {
                    if (currentSubmissionPackage != null && currentSubmissionPackage.equals(existingSubmissionPackage))
                    {
                        continue;
                    }
                    if (existingSubmissionPackage.getName().equalsIgnoreCase(contents.trim()))
                    {
                        this.errorMessage = "A Submission Package with the provided name already exists.";
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
        }, true, this.currentSubmissionPackage == null ? "" : this.currentSubmissionPackage.getName());

        this.nameField.getControl().addKeyListener(this);

        final Label descriptionLabel = new Label(this.container, SWT.NULL);
        descriptionLabel.setText("Description");

        this.descriptionField = stringValidatorToolkit.createField(new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL),
                new MaximumFieldLengthValidator("Description", 255), false, this.currentSubmissionPackage == null ? "" : this.currentSubmissionPackage.getDescription());
        this.descriptionField.getControl().addKeyListener(this);

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.nameField.getControl().setLayoutData(singleLineGridData);
        this.descriptionField.getControl().setLayoutData(multiLineGridData);

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
        setPageComplete(this.nameField.isValid() && this.descriptionField.isValid());
    }

    public String getSubmissionPackageName()
    {
        return this.nameField.getContents().trim();
    }

    public String getSubmissionPackageDescription()
    {
        return this.descriptionField.getContents().trim();
    }
}
