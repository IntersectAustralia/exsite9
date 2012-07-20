/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newproject;

import java.io.File;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.wizard.MaximumFieldLengthValidator;
import au.org.intersect.exsite9.wizard.WizardFieldUtils;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * The second page of the new project wizard.
 */
public final class EditOrCreateProjectWizardPage2 extends WizardPage implements KeyListener, SelectionListener
{
    private Composite container;

    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private ValidatingField<String> schemaNameField;
    private ValidatingField<String> schemaDescriptionField;
    private ValidatingField<String> schemaNamespaceURLField;
    private ValidatingField<String> importedSchemaLocationField;

    private Button localSchemaRadioButton;
    private Button importSchemaRadioButton;
    private Button importSchemaBrowseButton;

    /**
     * Constructor
     */
    public EditOrCreateProjectWizardPage2(final String pageTitle, final String pageDescription)
    {
        super(pageTitle);
        setTitle(pageTitle);
        setDescription(pageDescription);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout containerLayout = new GridLayout(1, true);
        this.container.setLayout(containerLayout);

        final Group localGroup = new Group(this.container, SWT.SHADOW_ETCHED_IN);
        localGroup.setText("Local");

        final Group importGroup = new Group(this.container, SWT.SHADOW_ETCHED_IN);
        importGroup.setText("Import");

        final GridLayout localGroupLayout = new GridLayout();
        final GridData localGroupLayoutData = new GridData(GridData.FILL_HORIZONTAL);
        localGroupLayout.numColumns = 2;
        localGroup.setLayout(localGroupLayout);
        localGroup.setLayoutData(localGroupLayoutData);

        final GridLayout importGroupLayout = new GridLayout();
        final GridData importGroupLayoutData = new GridData(GridData.FILL_HORIZONTAL);
        importGroupLayout.numColumns = 1;
        importGroup.setLayout(importGroupLayout);
        importGroup.setLayoutData(importGroupLayoutData);

        this.localSchemaRadioButton = new Button(localGroup, SWT.RADIO);
        this.localSchemaRadioButton.setText("Use a local Schema");
        this.localSchemaRadioButton.addSelectionListener(this);
        // Empty label in col2
        new Label(localGroup, SWT.NULL);

        this.stringValidatorToolkit = new StringValidationToolkit(SWT.TOP | SWT.LEFT, 1, true);
        this.stringValidatorToolkit.setDefaultErrorMessageHandler(this.errorMessageHandler);

        final Label schemaNameLabel = new Label(localGroup, SWT.NULL);
        schemaNameLabel.setText("Schema Name");

        schemaNameField = this.stringValidatorToolkit.createTextField(localGroup, new IFieldValidator<String>()
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
                    this.errorMessage = "Schema name must not be empty.";
                    return false;
                }

                if (contents.trim().length() >= 255)
                {
                    this.errorMessage = "Schema name is too long.";
                    return false;
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
        this.schemaNameField.getControl().addKeyListener(this);

        final Label schemaDescriptionLabel = new Label(localGroup, SWT.NULL);
        schemaDescriptionLabel.setText("Schema Description");

        this.schemaDescriptionField = stringValidatorToolkit.createField(new Text(localGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL),
                new MaximumFieldLengthValidator("Description", 255), false, "");
        this.schemaDescriptionField.getControl().addKeyListener(this);

        // 3 empty cells due to the description field spanning 4 rows below
        new Label(localGroup, SWT.NULL);
        new Label(localGroup, SWT.NULL);
        new Label(localGroup, SWT.NULL);

        final Label schemaNamespaceURLLabel = new Label(localGroup, SWT.NULL);
        schemaNamespaceURLLabel.setText("Schema Namespace URL");

        this.schemaNamespaceURLField = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, localGroup, "Namespace URL", "");
        this.schemaNamespaceURLField.getControl().addKeyListener(this);

        final Composite importSchemaRadioButtonContainer = new Composite(importGroup, SWT.NULL);
        importSchemaRadioButtonContainer.setLayout(new RowLayout(SWT.VERTICAL));
        this.importSchemaRadioButton = new Button(importSchemaRadioButtonContainer, SWT.RADIO);
        this.importSchemaRadioButton.setText("Import a Schema");
        this.importSchemaRadioButton.addSelectionListener(this);

        final Composite importSchemaLocationContainer = new Composite(importGroup, SWT.NULL);
        final GridLayout importSchemaLocationLayout = new GridLayout();
        final GridData importSchemaLocationLayoutData = new GridData(GridData.FILL_HORIZONTAL);
        importSchemaLocationLayout.numColumns = 3;
        importSchemaLocationContainer.setLayout(importSchemaLocationLayout);
        importSchemaLocationContainer.setLayoutData(importSchemaLocationLayoutData);

        final Label importedSchemaLocationLabel = new Label(importSchemaLocationContainer, SWT.NULL);
        importedSchemaLocationLabel.setText("Location");

        this.importedSchemaLocationField = stringValidatorToolkit.createTextField(importSchemaLocationContainer, new IFieldValidator<String>()
        {
            @Override
            public String getErrorMessage()
            {
                return "Imported schema location is not a valid file";
            }

            @Override
            public String getWarningMessage()
            {
                return null;
            }

            @Override
            public boolean isValid(final String arg)
            {
                final File file = new File(arg);
                return file.exists();
            }

            @Override
            public boolean warningExist(final String arg)
            {
                return false;
            }
        }, true, "");

        this.importSchemaBrowseButton = new Button(importSchemaLocationContainer, SWT.PUSH);
        this.importSchemaBrowseButton.setText("Browse");
        this.importSchemaBrowseButton.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e)
            {
            }
        });

        final GridData indentedGridData = new GridData();
        indentedGridData.horizontalIndent = 10;
        final GridData indentedGridData2 = new GridData();
        indentedGridData2.horizontalIndent = 10;
        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);
        multiLineGridData.verticalSpan = 4;

        this.schemaNameField.getControl().setLayoutData(singleLineGridData);
        this.schemaDescriptionField.getControl().setLayoutData(multiLineGridData);
        this.schemaNamespaceURLField.getControl().setLayoutData(singleLineGridData);
        this.importedSchemaLocationField.getControl().setLayoutData(singleLineGridData);

        schemaNameLabel.setLayoutData(indentedGridData);
        schemaDescriptionLabel.setLayoutData(indentedGridData);
        schemaNamespaceURLLabel.setLayoutData(indentedGridData);
        importedSchemaLocationLabel.setLayoutData(indentedGridData2);

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
     * @{inheritDoc
     */
    @Override
    public void keyReleased(final KeyEvent e)
    {
        setPageComplete(allFieldsAreValid());
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void widgetSelected(final SelectionEvent e)
    {
        if (e.widget == this.localSchemaRadioButton)
        {
            this.importSchemaRadioButton.setSelection(false);
            this.schemaNameField.getControl().setEnabled(true);
            this.schemaDescriptionField.getControl().setEnabled(true);
            this.schemaNamespaceURLField.getControl().setEnabled(true);
            this.importedSchemaLocationField.getControl().setEnabled(false);
            this.importSchemaBrowseButton.setEnabled(false);
        }
        else if (e.widget == this.importSchemaRadioButton)
        {
            this.localSchemaRadioButton.setSelection(false);
            this.schemaNameField.getControl().setEnabled(false);
            this.schemaDescriptionField.getControl().setEnabled(false);
            this.schemaNamespaceURLField.getControl().setEnabled(false);
            this.importedSchemaLocationField.getControl().setEnabled(true);
            this.importSchemaBrowseButton.setEnabled(true);
        }
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void widgetDefaultSelected(final SelectionEvent e)
    {
    }

    private boolean allFieldsAreValid()
    {
        return this.schemaNameField.isValid() && this.schemaDescriptionField.isValid() && this.schemaNamespaceURLField.isValid();
    }
}
