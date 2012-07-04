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

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.wizard.MaximumFieldLengthValidator;
import au.org.intersect.exsite9.wizard.WizardFieldUtils;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * The first page of the new project wizard.
 */
public final class EditOrCreateProjectWizardPage1 extends WizardPage implements KeyListener
{
    private ValidatingField<String> projectNameField;
    private ValidatingField<String> projectDescriptionText;
    private ValidatingField<String> projectOwnerText;
    private Combo projectCollectionDropDown;
    private ValidatingField<String> projectRightsStatementText;
    private ValidatingField<String> projectAccessRightsText;
    private ValidatingField<String> projectLicenceText;
    private ValidatingField<String> projectIdentifierText;
    private ValidatingField<String> projectSubjectText;
    private ValidatingField<String> projectElectronicLocationText;
    private ValidatingField<String> projectPhysicalLocationText;
    private ValidatingField<String> projectPlaceOrRegionNameText;
    private ValidatingField<String> projectLatitudeLongitudeText;
    private ValidatingField<String> projectDatesOfCaptureText;
    private ValidatingField<String> projectCitationInformationText;
    private ValidatingField<String> projectRelatedPartyText;
    private ValidatingField<String> projectRelatedActivityText;
    private ValidatingField<String> projectRelatedInformationText;

    private ProjectFieldsDTO incomingFieldValues;

    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private Composite container;

    /**
     * Constructor
     */
    public EditOrCreateProjectWizardPage1(String pageTitle, String pageDescription, ProjectFieldsDTO incomingFieldValues)
    {
        super(pageTitle);
        setTitle(pageTitle);
        setDescription(pageDescription);
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
        this.stringValidatorToolkit.setDefaultErrorMessageHandler(this.errorMessageHandler);

        final Label projectNameLabel = new Label(this.container, SWT.NULL);
        projectNameLabel.setText("Project Name");

        projectNameField = this.stringValidatorToolkit.createTextField(this.container, new IFieldValidator<String>()
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
                    this.errorMessage = "Project name must not be empty.";
                    return false;
                }

                if (contents.trim().length() >= 255)
                {
                    this.errorMessage = "Project name is too long.";
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
        }, true, this.incomingFieldValues.getName());

        this.projectNameField.getControl().addKeyListener(this);

        final Label projectOwnerLabel = new Label(this.container, SWT.NULL);
        projectOwnerLabel.setText("Project Owner");

        this.projectOwnerText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Owner", this.incomingFieldValues.getOwner());
        this.projectOwnerText.getControl().addKeyListener(this);

        final Label projectDescriptionLabel = new Label(this.container, SWT.NULL);
        projectDescriptionLabel.setText("Project Description");

        this.projectDescriptionText = stringValidatorToolkit.createField(new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL), new MaximumFieldLengthValidator("Description", 255), false, this.incomingFieldValues.getDescription());
        this.projectDescriptionText.getControl().addKeyListener(this);
        
     // 3 empty cells due to the description field spanning 4 rows below
        new Label(container, SWT.NULL);
        new Label(container, SWT.NULL);
        new Label(container, SWT.NULL);

        final Label collectionTypeLabel = new Label(this.container, SWT.NULL);
        collectionTypeLabel.setText("Collection Type");

        this.projectCollectionDropDown = new Combo(this.container, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.SINGLE);
        this.projectCollectionDropDown.setItems(new String[] {"", "Dataset", "Collection"});
        for (int i = 0; i < this.projectCollectionDropDown.getItemCount(); i++)
        {
            if (this.projectCollectionDropDown.getItem(i)
                    .equalsIgnoreCase(this.incomingFieldValues.getCollectionType()))
            {
                this.projectCollectionDropDown.select(i);
            }
        }
        this.projectCollectionDropDown.addSelectionListener(new SelectionListener()
        {
            
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setPageComplete(allFieldsAreValid());                
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
      
            }
        });

        final Label rightsStatementLabel = new Label(this.container, SWT.NULL);
        rightsStatementLabel.setText("Rights Statement");

        this.projectRightsStatementText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Rights Statement", this.incomingFieldValues.getRightsStatement());
        this.projectRightsStatementText.getControl().addKeyListener(this);

        final Label accessRightsLabel = new Label(this.container, SWT.NULL);
        accessRightsLabel.setText("Access Rights");

        this.projectAccessRightsText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Access Rights", this.incomingFieldValues.getAccessRights());
        this.projectAccessRightsText.getControl().addKeyListener(this);

        final Label licenceLabel = new Label(this.container, SWT.NULL);
        licenceLabel.setText("Licence");

        this.projectLicenceText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Licence", this.incomingFieldValues.getLicence());
        this.projectLicenceText.getControl().addKeyListener(this);

        final Label identifierLabel = new Label(this.container, SWT.NULL);
        identifierLabel.setText("Identifier");

        this.projectIdentifierText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Identifier", this.incomingFieldValues.getIdentifier());
        this.projectIdentifierText.getControl().addKeyListener(this);

        final Label subjectLabel = new Label(this.container, SWT.NULL);
        subjectLabel.setText("Subject");

        this.projectSubjectText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Subject", this.incomingFieldValues.getSubject());
        this.projectSubjectText.getControl().addKeyListener(this);

        final Label electronicLocationLabel = new Label(this.container, SWT.NULL);
        electronicLocationLabel.setText("Electronic Location");

        this.projectElectronicLocationText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Electronic Location", this.incomingFieldValues.getElectronicLocation());
        this.projectElectronicLocationText.getControl().addKeyListener(this);

        final Label physicalLocationLabel = new Label(this.container, SWT.NULL);
        physicalLocationLabel.setText("Physical Location");

        this.projectPhysicalLocationText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Physical Location", this.incomingFieldValues.getPhysicalLocation());
        this.projectPhysicalLocationText.getControl().addKeyListener(this);

        final Label placeOrRegionNameLabel = new Label(this.container, SWT.NULL);
        placeOrRegionNameLabel.setText("Place or Region Name");

        this.projectPlaceOrRegionNameText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Place or Region Name", this.incomingFieldValues.getPlaceOrRegionName());
        this.projectPlaceOrRegionNameText.getControl().addKeyListener(this);

        final Label latitudeLongitudeLabel = new Label(this.container, SWT.NULL);
        latitudeLongitudeLabel.setText("Latitude/Longitude");

        this.projectLatitudeLongitudeText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Latitude/Longitude", this.incomingFieldValues.getLatitudeLongitude());
        this.projectLatitudeLongitudeText.getControl().addKeyListener(this);

        final Label datesOfCaptureLabel = new Label(this.container, SWT.NULL);
        datesOfCaptureLabel.setText("Dates of Capture");

        this.projectDatesOfCaptureText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Dates of Capture", this.incomingFieldValues.getDatesOfCapture());
        this.projectDatesOfCaptureText.getControl().addKeyListener(this);

        final Label citationInformationLabel = new Label(this.container, SWT.NULL);
        citationInformationLabel.setText("Citation Information");

        this.projectCitationInformationText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Citation Information", this.incomingFieldValues.getCitationInformation());
        this.projectCitationInformationText.getControl().addKeyListener(this);

        final Label relatedPartyLabel = new Label(this.container, SWT.NULL);
        relatedPartyLabel.setText("Related Party");

        this.projectRelatedPartyText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Related Party", this.incomingFieldValues.getRelatedParty());
        this.projectRelatedPartyText.getControl().addKeyListener(this);

        final Label relatedActivityLabel = new Label(this.container, SWT.NULL);
        relatedActivityLabel.setText("Related Activity");

        this.projectRelatedActivityText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Related Activity", this.incomingFieldValues.getRelatedActivity());
        this.projectRelatedActivityText.getControl().addKeyListener(this);

        final Label relatedInformationLabel = new Label(this.container, SWT.NULL);
        relatedInformationLabel.setText("Related Information");

        this.projectRelatedInformationText = WizardFieldUtils.createOptional255TextField(stringValidatorToolkit, this.container, "Related Information", this.incomingFieldValues.getRelatedInformation());
        this.projectRelatedInformationText.getControl().addKeyListener(this);

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);
        multiLineGridData.verticalSpan = 4;

        this.projectNameField.getControl().setLayoutData(singleLineGridData);
        this.projectOwnerText.getControl().setLayoutData(singleLineGridData);
        this.projectDescriptionText.getControl().setLayoutData(multiLineGridData);
        this.projectCollectionDropDown.setLayoutData(singleLineGridData);
        this.projectRightsStatementText.getControl().setLayoutData(singleLineGridData);
        this.projectAccessRightsText.getControl().setLayoutData(singleLineGridData);
        this.projectLicenceText.getControl().setLayoutData(singleLineGridData);
        this.projectIdentifierText.getControl().setLayoutData(singleLineGridData);
        this.projectSubjectText.getControl().setLayoutData(singleLineGridData);
        this.projectElectronicLocationText.getControl().setLayoutData(singleLineGridData);
        this.projectPhysicalLocationText.getControl().setLayoutData(singleLineGridData);
        this.projectPlaceOrRegionNameText.getControl().setLayoutData(singleLineGridData);
        this.projectLatitudeLongitudeText.getControl().setLayoutData(singleLineGridData);
        this.projectDatesOfCaptureText.getControl().setLayoutData(singleLineGridData);
        this.projectCitationInformationText.getControl().setLayoutData(singleLineGridData);
        this.projectRelatedPartyText.getControl().setLayoutData(singleLineGridData);
        this.projectRelatedActivityText.getControl().setLayoutData(singleLineGridData);
        this.projectRelatedInformationText.getControl().setLayoutData(singleLineGridData);

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
        setPageComplete(allFieldsAreValid());
    }

    public ProjectFieldsDTO getProjectFields()
    {
        return new ProjectFieldsDTO(this.projectNameField.getContents().trim(), 
                this.projectOwnerText.getContents().trim(), this.projectDescriptionText.getContents().trim(),                
                this.projectCollectionDropDown.getItem(this.projectCollectionDropDown.getSelectionIndex()),
                this.projectRightsStatementText.getContents().trim(), this.projectAccessRightsText.getContents().trim(),
                this.projectLicenceText.getContents().trim(), this.projectIdentifierText.getContents().trim(),
                this.projectSubjectText.getContents().trim(), this.projectElectronicLocationText.getContents().trim(),
                this.projectPhysicalLocationText.getContents().trim(), this.projectPlaceOrRegionNameText.getContents().trim(),
                this.projectLatitudeLongitudeText.getContents().trim(), this.projectDatesOfCaptureText.getContents().trim(),
                this.projectCitationInformationText.getContents().trim(), this.projectRelatedPartyText.getContents().trim(),
                this.projectRelatedActivityText.getContents().trim(), this.projectRelatedInformationText.getContents().trim());
    }
    
    private boolean allFieldsAreValid()
    {
        return this.projectNameField.isValid() &&
                this.projectDescriptionText.isValid() &&
                this.projectOwnerText.isValid() &&
                this.projectRightsStatementText.isValid() &&
                this.projectAccessRightsText.isValid() &&
                this.projectLicenceText.isValid() &&
                this.projectIdentifierText.isValid() &&
                this.projectSubjectText.isValid() &&
                this.projectElectronicLocationText.isValid() &&
                this.projectPhysicalLocationText.isValid() &&
                this.projectPlaceOrRegionNameText.isValid() &&
                this.projectLatitudeLongitudeText.isValid() &&
                this.projectDatesOfCaptureText.isValid() &&
                this.projectCitationInformationText.isValid() &&
                this.projectRelatedPartyText.isValid() &&
                this.projectRelatedActivityText.isValid() &&
                this.projectRelatedInformationText.isValid();
    }
}
