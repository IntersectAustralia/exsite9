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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.xml.sax.SAXException;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;

import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.exception.InvalidSchemaException;
import au.org.intersect.exsite9.service.ISchemaService;
import au.org.intersect.exsite9.wizard.MaximumLengthFieldValidator;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * The second page of the new project wizard.
 */
public final class EditOrCreateProjectWizardPage2 extends WizardPage implements KeyListener, SelectionListener
{
    private static final Logger LOG = Logger.getLogger(EditOrCreateProjectWizardPage2.class);

    private Composite container;

    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private ValidatingField<String> localSchemaNameField;
    private Text localSchemaDescriptionField;
    private Text localSchemaNamespaceURLField;

    private Text importedSchemaNameField;
    private Text importedSchemaDescriptionField;
    private Text importedSchemaNamespaceURLField;

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
        localGroup.setText(this.currentSchema == null ? "Create" : "Edit");

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
        this.localSchemaRadioButton.setText(this.currentSchema == null ? "Create a new schema" : "Edit a local schema");
        this.localSchemaRadioButton.addSelectionListener(this);
        // Empty label in col2
        new Label(localGroup, SWT.NULL);

        this.stringValidatorToolkit = new StringValidationToolkit(SWT.TOP | SWT.LEFT, 1, true);
        this.stringValidatorToolkit.setDefaultErrorMessageHandler(this.errorMessageHandler);

        final Label localSchemaNameLabel = new Label(localGroup, SWT.NULL);
        localSchemaNameLabel.setText("Schema Name");

        this.localSchemaNameField = this.stringValidatorToolkit.createTextField(localGroup, new MaximumLengthFieldValidator("Schema Name", 255), true, "");
        this.localSchemaNameField.getControl().addKeyListener(this);

        final Label localSchemaDescriptionLabel = new Label(localGroup, SWT.NULL);
        localSchemaDescriptionLabel.setText("Schema Description");

        this.localSchemaDescriptionField = new Text(localGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        this.localSchemaDescriptionField.addKeyListener(this);

        // 3 empty cells due to the description field spanning 4 rows below
        new Label(localGroup, SWT.NULL);
        new Label(localGroup, SWT.NULL);
        new Label(localGroup, SWT.NULL);

        final Label localSchemaNamespaceURLLabel = new Label(localGroup, SWT.NULL);
        localSchemaNamespaceURLLabel.setText("Schema Namespace URL");

        this.localSchemaNamespaceURLField = new Text(localGroup, SWT.SINGLE | SWT.BORDER);
        this.localSchemaNamespaceURLField.addKeyListener(this);

        final Composite importSchemaRadioButtonContainer = new Composite(importGroup, SWT.NULL);
        importSchemaRadioButtonContainer.setLayout(new RowLayout(SWT.VERTICAL));
        this.importSchemaRadioButton = new Button(importSchemaRadioButtonContainer, SWT.RADIO);
        this.importSchemaRadioButton.setText("Import an existing schema");
        this.importSchemaRadioButton.addSelectionListener(this);

        final Composite importSchemaContainer = new Composite(importGroup, SWT.NULL);
        final GridLayout importSchemaLayout = new GridLayout();
        final GridData importSchemaLayoutData = new GridData(GridData.FILL_HORIZONTAL);
        importSchemaLayout.numColumns = 2;
        importSchemaContainer.setLayout(importSchemaLayout);
        importSchemaContainer.setLayoutData(importSchemaLayoutData);

        final Label importedSchemaNameLabel = new Label(importSchemaContainer, SWT.NULL);
        importedSchemaNameLabel.setText("Schema Name");

        this.importedSchemaNameField = new Text(importSchemaContainer, SWT.BORDER | SWT.SINGLE);
        this.importedSchemaNameField.setEditable(false);
        this.importedSchemaNameField.setEnabled(false);

        final Label importedSchemaDescriptionLabel = new Label(importSchemaContainer, SWT.NULL);
        importedSchemaDescriptionLabel.setText("Schema Description");

        this.importedSchemaDescriptionField = new Text(importSchemaContainer, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        this.importedSchemaDescriptionField.setEditable(false);
        this.importedSchemaDescriptionField.setEnabled(false);

        // 3 empty cells due to the description field spanning 4 rows below
        new Label(importSchemaContainer, SWT.NULL);
        new Label(importSchemaContainer, SWT.NULL);
        new Label(importSchemaContainer, SWT.NULL);

        final Label importedSchemaNamespaceURLLabel = new Label(importSchemaContainer, SWT.NULL);
        importedSchemaNamespaceURLLabel.setText("Schema Namespace URL");

        this.importedSchemaNamespaceURLField = new Text(importSchemaContainer, SWT.BORDER | SWT.SINGLE);
        this.importedSchemaNamespaceURLField.setEditable(false);
        this.importedSchemaNamespaceURLField.setEnabled(false);

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
                    loadSchema(true, new File(filePath));
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
        this.localSchemaDescriptionField.setLayoutData(multiLineGridData);
        this.localSchemaNamespaceURLField.setLayoutData(singleLineGridData);
        this.importedSchemaNameField.setLayoutData(singleLineGridData);
        this.importedSchemaDescriptionField.setLayoutData(multiLineGridData);
        this.importedSchemaNamespaceURLField.setLayoutData(singleLineGridData);
        this.importSchemaBrowseButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        localSchemaNameLabel.setLayoutData(indentedGridData);
        localSchemaDescriptionLabel.setLayoutData(indentedGridData);
        localSchemaNamespaceURLLabel.setLayoutData(indentedGridData);

        this.localSchemaNameField.getControl().setEnabled(false);
        this.localSchemaDescriptionField.setEnabled(false);
        this.localSchemaNamespaceURLField.setEnabled(false);
        this.importSchemaBrowseButton.setEnabled(false);

        if (this.currentSchema != null)
        {
            final boolean localSchema = this.currentSchema.getLocal();
            final String schemaName = this.currentSchema.getName();
            final String schemaDescription = this.currentSchema.getDescription();
            final String schemaNamespaceURL = this.currentSchema.getNamespaceURL();
            if (localSchema)
            {
                this.localSchemaNameField.setContents(schemaName);
                this.localSchemaDescriptionField.setText(schemaDescription);
                this.localSchemaNamespaceURLField.setText(schemaNamespaceURL);
                enableLocalSchemaFields();
            }
            else
            {
                importedSchemaNameField.setText(schemaName);
                importedSchemaDescriptionField.setText(schemaDescription);
                importedSchemaNamespaceURLField.setText(schemaNamespaceURL);
                enableImportedSchemaFields();
            }
        }
        else
        {
            // Load the default Schema.
            final File defaultSchema = this.schemaService.getDefaultSchema();
            if (defaultSchema != null)
            {
                final boolean successLoad = loadSchema(false, defaultSchema);
                if (successLoad)
                {
                    importSchemaRadioButton.setSelection(true);
                }
            }
        }

        setControl(this.container);
        setPageComplete(allFieldsAreValid());
        this.errorMessageHandler.clearMessage();
    }

    private boolean loadSchema(final boolean showErrors, final File schemaFile)
    {
        try
        {
            importedSchema = schemaService.parseSchema(schemaFile);
            importedSchemaNameField.setText(importedSchema.getName());
            importedSchemaDescriptionField.setText(importedSchema.getDescription());
            importedSchemaNamespaceURLField.setText(importedSchema.getNamespaceURL());
            setPageComplete(allFieldsAreValid());
            return true;
        }
        catch (final SAXException e)
        {
            if (showErrors)
            {
                MessageDialog.openError(getShell(), "Could not import Schema", "The XML file is an invalid metadata schema. Reason: " + e.getMessage());
            }
            LOG.error(e);
        }
        catch (final IOException e)
        {
            if (showErrors)
            {
                MessageDialog.openError(getShell(), "Could not import Schema", "Error reading the metadata schema. Reason: " + e.getMessage());
            }
            LOG.error(e);
        }
        catch (final ParserConfigurationException e)
        {
            if (showErrors)
            {
                MessageDialog.openError(getShell(), "Could not import Schema", "Error parsing the metadata schema. Reason: " + e.getMessage());
            }
            LOG.error(e);
        }
        catch (final InvalidSchemaException e)
        {
            if (showErrors)
            {
                MessageDialog.openError(getShell(), "Could not import Schema", "This XML file is an invalid metadata schema. Reason: " + e.getMessage());
            }
        }
        return false;
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

    private void enableLocalSchemaFields()
    {
        this.localSchemaRadioButton.setSelection(true);
        this.importSchemaRadioButton.setSelection(false);
        this.localSchemaNameField.getControl().setEnabled(true);
        this.localSchemaDescriptionField.setEnabled(true);
        this.localSchemaNamespaceURLField.setEnabled(true);
        this.importSchemaBrowseButton.setEnabled(false);
    }

    private void enableImportedSchemaFields()
    {
        this.localSchemaRadioButton.setSelection(false);
        this.importSchemaRadioButton.setSelection(true);
        this.localSchemaNameField.getControl().setEnabled(false);
        this.localSchemaDescriptionField.setEnabled(false);
        this.localSchemaNamespaceURLField.setEnabled(false);
        this.importSchemaBrowseButton.setEnabled(true);
        this.importSchemaBrowseButton.setFocus();
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
            enableLocalSchemaFields();
        }
        else if (e.widget == this.importSchemaRadioButton)
        {
            enableImportedSchemaFields();
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
            return this.localSchemaNameField.isValid() && !this.localSchemaNameField.getContents().isEmpty();
        }
        if (this.importSchemaRadioButton.getSelection())
        {
            return (this.importedSchema != null) && (! this.importedSchema.getLocal());
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
        return this.localSchemaDescriptionField.getText().trim();
    }

    public String getLocalSchemaNamespaceURL()
    {
        return this.localSchemaNamespaceURLField.getText().trim();
    }

    public Schema getImportedSchema()
    {
        return this.importedSchema;
    }
}
