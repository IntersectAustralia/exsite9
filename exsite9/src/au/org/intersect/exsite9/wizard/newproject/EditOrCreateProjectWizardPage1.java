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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.validators.ProjectNameValidator;
import au.org.intersect.exsite9.validators.NonEmptyValidator;

/**
 * The first page of the new project wizard.
 */
public final class EditOrCreateProjectWizardPage1 extends WizardPage implements KeyListener
{
    private ValidatingField<String> projectNameText;
    private IFieldValidator<String> projectNameValidator;
    private ValidatingField<String> ownerText;
    private IFieldValidator<String> ownerValidator;
    private ValidatingField<String> emailText;
    private IFieldValidator<String> emailValidator;
    private Text institutionText;
    private Text descriptionText;
    private Combo collectionDropDown;
    private Text rightsStatementText;
    private Text accessRightsText;
    private Text licenceText;
    private Text identifierText;
    private Text subjectText;

    private ProjectFieldsDTO incomingFieldValues;

    private StringValidationToolkit stringValidatorToolkit;

    private Composite container;

    /**
     * Constructor
     */
    public EditOrCreateProjectWizardPage1(final String pageTitle, final String pageDescription, final ProjectFieldsDTO incomingFieldValues)
    {
        super(pageTitle);
        super.setTitle(pageTitle);
        super.setDescription(pageDescription);
        this.incomingFieldValues = incomingFieldValues;
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

        final Label identifierLabel = new Label(this.container, SWT.NULL);
        identifierLabel.setText("Identifier");

        this.identifierText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.identifierText.setText(this.incomingFieldValues.getIdentifier());

        final Label projectNameLabel = new Label(this.container, SWT.NULL);
        projectNameLabel.setText("Project Name");

        this.projectNameValidator = new ProjectNameValidator();
        this.projectNameText = this.stringValidatorToolkit.createTextField(this.container, this.projectNameValidator, true, this.incomingFieldValues.getName());
        this.projectNameText.getControl().addKeyListener(this);

        final Label nameLabel = new Label(this.container, SWT.NULL);
        nameLabel.setText("Name");

        this.ownerValidator = new NonEmptyValidator("Name");
        this.ownerText = this.stringValidatorToolkit.createTextField(this.container, this.ownerValidator, true, this.incomingFieldValues.getOwner());
        this.ownerText.getControl().addKeyListener(this);

        final Label institutionLabel = new Label(this.container, SWT.NULL);
        institutionLabel.setText("Institution");

        this.institutionText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.institutionText.setText(this.incomingFieldValues.getInstitution());

        final Label emailLabel = new Label(this.container, SWT.NULL);
        emailLabel.setText("Email");

        this.emailValidator = new NonEmptyValidator("Email");
        this.emailText = this.stringValidatorToolkit.createTextField(this.container, this.emailValidator, true, this.incomingFieldValues.getEmail());
        this.emailText.getControl().addKeyListener(this);
        
        final Label projectDescriptionLabel = new Label(this.container, SWT.NULL);
        projectDescriptionLabel.setText("Description");

        this.descriptionText = new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        this.descriptionText.setText(this.incomingFieldValues.getDescription());
        
        // 3 empty cells due to the description field spanning 4 rows below
        new Label(container, SWT.NULL);
        new Label(container, SWT.NULL);
        new Label(container, SWT.NULL);

        final Label collectionTypeLabel = new Label(this.container, SWT.NULL);
        collectionTypeLabel.setText("Collection Type");

        this.collectionDropDown = new Combo(this.container, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.SINGLE);
        this.collectionDropDown.setItems(new String[] {"", "Dataset", "Collection"});
        for (int i = 0; i < this.collectionDropDown.getItemCount(); i++)
        {
            if (this.collectionDropDown.getItem(i).equalsIgnoreCase(this.incomingFieldValues.getCollectionType()))
            {
                this.collectionDropDown.select(i);
            }
        }

        this.collectionDropDown.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                setPageComplete(allFieldsAreValid());                
            }
            
            @Override
            public void widgetDefaultSelected(final SelectionEvent e)
            {
            }
        });

        final Label rightsStatementLabel = new Label(this.container, SWT.NULL);
        rightsStatementLabel.setText("Rights Statement");

        this.rightsStatementText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.rightsStatementText.setText(this.incomingFieldValues.getRightsStatement());

        final Label accessRightsLabel = new Label(this.container, SWT.NULL);
        accessRightsLabel.setText("Access Rights");

        this.accessRightsText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.accessRightsText.setText(this.incomingFieldValues.getAccessRights());

        final Label licenceLabel = new Label(this.container, SWT.NULL);
        licenceLabel.setText("Licence");

        this.licenceText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.licenceText.setText(this.incomingFieldValues.getLicence());

        final Label subjectLabel = new Label(this.container, SWT.NULL);
        subjectLabel.setText("Subject");

        this.subjectText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.subjectText.setText(this.incomingFieldValues.getSubject());


        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);
        multiLineGridData.verticalSpan = 4;

        this.projectNameText.getControl().setLayoutData(singleLineGridData);
        this.ownerText.getControl().setLayoutData(singleLineGridData);
        this.institutionText.setLayoutData(singleLineGridData);
        this.emailText.getControl().setLayoutData(singleLineGridData);
        this.descriptionText.setLayoutData(multiLineGridData);
        this.collectionDropDown.setLayoutData(singleLineGridData);
        this.rightsStatementText.setLayoutData(singleLineGridData);
        this.accessRightsText.setLayoutData(singleLineGridData);
        this.licenceText.setLayoutData(singleLineGridData);
        this.identifierText.setLayoutData(singleLineGridData);
        this.subjectText.setLayoutData(singleLineGridData);

        setControl(this.container);
        setPageComplete(allFieldsAreValid());
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
        setPageComplete(allFieldsAreValid());
    }

    // Not to override super.getName()
    public String getProjectName()
    {
        return this.projectNameText.getContents().trim();
    }

    public String getOwner()
    {
        return this.ownerText.getContents().trim();
    }

    public String getInstitution()
    {
        return this.institutionText.getText().trim();
    }

    public String getEmail()
    {
        return this.emailText.getContents().trim();
    }

    // Not to override super.getDescription()
    public String getProjectDescription()
    {
        return this.descriptionText.getText().trim();
    }

    public String getCollectionType()
    {
        return this.collectionDropDown.getItem(this.collectionDropDown.getSelectionIndex());
    }

    public String getRightsStatement()
    {
        return this.rightsStatementText.getText().trim();
    }

    public String getAccessRights()
    {
        return this.accessRightsText.getText().trim();
    }

    public String getLicense()
    {
        return this.licenceText.getText().trim();
    }

    public String getIdentifier()
    {
        return this.identifierText.getText().trim();
    }

    public String getSubject()
    {
        return this.subjectText.getText().trim();
    }
    
    private boolean allFieldsAreValid()
    {
        if (!this.projectNameText.isValid())
        {
            setErrorMessage(this.projectNameValidator.getErrorMessage());
            return false;
        }
        if (!this.ownerText.isValid())
        {
            setErrorMessage(this.ownerValidator.getErrorMessage());
            return false;
        }
        if (!this.emailText.isValid())
        {
            setErrorMessage(this.emailValidator.getErrorMessage());
            return false;
        }

        setErrorMessage(null);
        return true;
    }
}
