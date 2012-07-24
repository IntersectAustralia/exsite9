/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newproject;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXException;

import com.google.common.base.Objects;
import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.service.ISchemaService;
import au.org.intersect.exsite9.wizard.MaximumFieldLengthValidator;
import au.org.intersect.exsite9.wizard.WizardFieldUtils;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * The second page of the new project wizard.
 */
public final class EditOrCreateProjectWizardPage2 extends WizardPage implements KeyListener, SelectionListener
{
    private static final Logger LOG = Logger.getLogger(EditOrCreateProjectWizardPage2.class);

    private static final String FORCED_EVENT = "FORCED EVENT";

    private Composite container;

    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private ValidatingField<String> localSchemaNameField;
    private ValidatingField<String> localSchemaDescriptionField;
    private ValidatingField<String> localSchemaNamespaceURLField;

    private Button localSchemaRadioButton;
    private Button importSchemaRadioButton;
    private Button importSchemaBrowseButton;

    private Schema importedSchema;
    private final Schema currentSchema;

    private final ISchemaService schemaService;

    /**
     * Constructor
     */
    public EditOrCreateProjectWizardPage2(final String pageTitle, final String pageDescription, final Schema currentSchema)
    {
        super(pageTitle);
        setTitle(pageTitle);
        setDescription(pageDescription);
        this.currentSchema = currentSchema;
        this.importedSchema = currentSchema;
        this.schemaService = (ISchemaService) PlatformUI.getWorkbench().getService(ISchemaService.class);
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

        final Label localSchemaNameLabel = new Label(localGroup, SWT.NULL);
        localSchemaNameLabel.setText("Schema Name");

        localSchemaNameField = this.stringValidatorToolkit.createTextField(localGroup, new IFieldValidator<String>()
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
        this.localSchemaNameField.getControl().addKeyListener(this);

        final Label localSchemaDescriptionLabel = new Label(localGroup, SWT.NULL);
        localSchemaDescriptionLabel.setText("Schema Description");

        this.localSchemaDescriptionField = stringValidatorToolkit.createField(new Text(localGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL),
                new MaximumFieldLengthValidator("Description", 255), false, "");
        this.localSchemaDescriptionField.getControl().addKeyListener(this);

        // 3 empty cells due to the description field spanning 4 rows below
        new Label(localGroup, SWT.NULL);
        new Label(localGroup, SWT.NULL);
        new Label(localGroup, SWT.NULL);

        final Label localSchemaNamespaceURLLabel = new Label(localGroup, SWT.NULL);
        localSchemaNamespaceURLLabel.setText("Schema Namespace URL");

        this.localSchemaNamespaceURLField = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, localGroup, "Namespace URL", "");
        this.localSchemaNamespaceURLField.getControl().addKeyListener(this);

        final Composite importSchemaRadioButtonContainer = new Composite(importGroup, SWT.NULL);
        importSchemaRadioButtonContainer.setLayout(new RowLayout(SWT.VERTICAL));
        this.importSchemaRadioButton = new Button(importSchemaRadioButtonContainer, SWT.RADIO);
        this.importSchemaRadioButton.setText("Import a Schema");
        this.importSchemaRadioButton.addSelectionListener(this);

        final Composite importSchemaContainer = new Composite(importGroup, SWT.NULL);
        final GridLayout importSchemaLayout = new GridLayout();
        final GridData importSchemaLayoutData = new GridData(GridData.FILL_HORIZONTAL);
        importSchemaLayout.numColumns = 2;
        importSchemaContainer.setLayout(importSchemaLayout);
        importSchemaContainer.setLayoutData(importSchemaLayoutData);

        final Label importedSchemaNameLabel = new Label(importSchemaContainer, SWT.NULL);
        importedSchemaNameLabel.setText("Schema Name");

        final Text importedSchemaNameField = new Text(importSchemaContainer, SWT.BORDER | SWT.SINGLE);
        importedSchemaNameField.setEditable(false);
        importedSchemaNameField.setEnabled(false);

        final Label importedSchemaDescriptionLabel = new Label(importSchemaContainer, SWT.NULL);
        importedSchemaDescriptionLabel.setText("Schema Description");

        final Text importedSchemaDescriptionField = new Text(importSchemaContainer, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        importedSchemaDescriptionField.setEditable(false);
        importedSchemaDescriptionField.setEnabled(false);

        // 3 empty cells due to the description field spanning 4 rows below
        new Label(importSchemaContainer, SWT.NULL);
        new Label(importSchemaContainer, SWT.NULL);
        new Label(importSchemaContainer, SWT.NULL);

        final Label importedSchemaNamespaceURLLabel = new Label(importSchemaContainer, SWT.NULL);
        importedSchemaNamespaceURLLabel.setText("Schema Namespace URL");

        final Text importedSchemaNamespaceURLField = new Text(importSchemaContainer, SWT.BORDER | SWT.SINGLE);
        importedSchemaNamespaceURLField.setEditable(false);
        importedSchemaNamespaceURLField.setEnabled(false);

        // 1 empty cell to push the browse button to the right cell.
        new Label(importSchemaContainer, SWT.NULL);

        this.importSchemaBrowseButton = new Button(importSchemaContainer, SWT.PUSH);
        this.importSchemaBrowseButton.setText("Browse");
        this.importSchemaBrowseButton.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent event)
            {
                final FileDialog fileDialog = new FileDialog(parent.getShell(), SWT.OPEN);
                fileDialog.setFilterExtensions(new String[]{"*.xml"});
                fileDialog.setFilterNames(new String[]{"Schema Files (*.xml)"});
                final File schemaDir = schemaService.getDefaultSchemaDirectory();
                if (schemaDir != null)
                {
                    fileDialog.setFilterPath(schemaDir.getAbsolutePath());
                }

                final String filePath = fileDialog.open();
                if (filePath != null)
                {
                    if (currentSchema != null && importedSchema != null)
                    {
                        final boolean confirm = MessageDialog.openConfirm(getShell(), "Are you sure you want to import a new schema?",
                            "Importing a new schema will cause all your current metadata to be overwritten and your associations will be lost. Are you sure you wish to proceed?");
                        if (!confirm)
                        {
                            return;
                        }
                    }

                    try
                    {
                        importedSchema = schemaService.parseSchema(new File(filePath));
                        importedSchemaNameField.setText(importedSchema.getName());
                        importedSchemaDescriptionField.setText(importedSchema.getDescription());
                        importedSchemaNamespaceURLField.setText(importedSchema.getNamespaceURL());
                        setPageComplete(allFieldsAreValid());
                    }
                    catch (final SAXException e)
                    {
                        MessageDialog.openError(getShell(), "Could not import Schema", e.getMessage());
                        LOG.error(e);
                    }
                    catch (final IOException e)
                    {
                        MessageDialog.openError(getShell(), "Could not import Schema", e.getMessage());
                        LOG.error(e);
                    }
                    catch (final ParserConfigurationException e)
                    {
                        MessageDialog.openError(getShell(), "Could not import Schema", e.getMessage());
                        LOG.error(e);
                    }
                }
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

        this.localSchemaNameField.getControl().setLayoutData(singleLineGridData);
        this.localSchemaDescriptionField.getControl().setLayoutData(multiLineGridData);
        this.localSchemaNamespaceURLField.getControl().setLayoutData(singleLineGridData);
        importedSchemaNameField.setLayoutData(singleLineGridData);
        importedSchemaDescriptionField.setLayoutData(multiLineGridData);
        importedSchemaNamespaceURLField.setLayoutData(singleLineGridData);
        this.importSchemaBrowseButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        localSchemaNameLabel.setLayoutData(indentedGridData);
        localSchemaDescriptionLabel.setLayoutData(indentedGridData);
        localSchemaNamespaceURLLabel.setLayoutData(indentedGridData);

        this.localSchemaNameField.getControl().setEnabled(false);
        this.localSchemaDescriptionField.getControl().setEnabled(false);
        this.localSchemaNamespaceURLField.getControl().setEnabled(false);
        this.importSchemaBrowseButton.setEnabled(false);

        if (this.currentSchema != null)
        {
            final boolean localSchema = this.currentSchema.getLocal();
            final String schemaName = this.currentSchema.getName();
            final String schemaDescription = this.currentSchema.getDescription();
            final String schemaNamespaceURL = this.currentSchema.getNamespaceURL();
            if (localSchema)
            {
                final Event event = new Event();
                event.widget = localSchemaRadioButton;
                event.text = FORCED_EVENT;
                widgetSelected(new SelectionEvent(event));

                this.localSchemaRadioButton.setSelection(true);
                this.importSchemaRadioButton.setSelection(false);
                this.localSchemaNameField.setContents(schemaName);
                this.localSchemaDescriptionField.setContents(schemaDescription);
                this.localSchemaNamespaceURLField.setContents(schemaNamespaceURL);
            }
            else
            {
                final Event event = new Event();
                event.widget = importSchemaRadioButton;
                event.text = FORCED_EVENT;
                widgetSelected(new SelectionEvent(event));

                this.localSchemaRadioButton.setSelection(false);
                this.importSchemaRadioButton.setSelection(true);
                importedSchemaNameField.setText(schemaName);
                importedSchemaDescriptionField.setText(schemaDescription);
                importedSchemaNamespaceURLField.setText(schemaNamespaceURL);
            }
        }

        setControl(this.container);
        setPageComplete(allFieldsAreValid());
        this.errorMessageHandler.clearMessage();
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
        this.errorMessageHandler.clearMessage();
        if (e.widget == this.localSchemaRadioButton)
        {
            if (this.currentSchema != null && this.importedSchema != null && !Objects.equal(e.text, FORCED_EVENT))
            {
                final boolean confirm = MessageDialog.openConfirm(getShell(), "Are you sure you want to use a local schema?",
                    "Using a local schema will cause all your current metadata to be removed. Are you sure you wish to proceed?");
                if (!confirm)
                {
                    this.importSchemaRadioButton.setSelection(true);
                    this.localSchemaRadioButton.setSelection(false);
                    return;
                }
            }
            this.importSchemaRadioButton.setSelection(false);
            this.localSchemaNameField.getControl().setEnabled(true);
            this.localSchemaDescriptionField.getControl().setEnabled(true);
            this.localSchemaNamespaceURLField.getControl().setEnabled(true);
            this.importSchemaBrowseButton.setEnabled(false);
        }
        else if (e.widget == this.importSchemaRadioButton)
        {
            this.localSchemaRadioButton.setSelection(false);
            this.localSchemaNameField.getControl().setEnabled(false);
            this.localSchemaDescriptionField.getControl().setEnabled(false);
            this.localSchemaNamespaceURLField.getControl().setEnabled(false);
            this.importSchemaBrowseButton.setEnabled(true);
        }
        setPageComplete(allFieldsAreValid());
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
        if (this.localSchemaRadioButton.getSelection())
        {
            return this.localSchemaNameField.isValid() && this.localSchemaDescriptionField.isValid() && this.localSchemaNamespaceURLField.isValid();
        }
        if (this.importSchemaRadioButton.getSelection())
        {
            return this.importedSchema != null;
        }
        return false;
    }

    public boolean getUseLocalSchema()
    {
        return this.localSchemaRadioButton.getSelection();
    }

    public String getLocalSchemaName()
    {
        return this.localSchemaNameField.getContents().trim();
    }

    public String getLocalSchemaDescription()
    {
        return this.localSchemaDescriptionField.getContents().trim();
    }

    public String getLocalSchemaNamespaceURL()
    {
        return this.localSchemaNamespaceURLField.getContents().trim();
    }

    public Schema getImportedSchema()
    {
        return this.importedSchema;
    }
}
