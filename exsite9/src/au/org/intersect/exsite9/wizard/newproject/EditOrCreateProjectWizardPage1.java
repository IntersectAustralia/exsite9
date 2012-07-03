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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * The first page of the new project wizard.
 */
public final class EditOrCreateProjectWizardPage1 extends WizardPage implements KeyListener
{
    private ValidatingField<String> projectNameField;

    private Text projectDescriptionText;
    private Text projectOwnerText;
    private Combo projectCollectionDropDown;
    private Text projectRightsStatementText;
    private Text projectAccessRightsText;
    private Text projectLicenceText;
    private Text projectIdentifierText;
    private Text projectSubjectText;
    private Text projectElectronicLocationText;
    private Text projectPhysicalLocationText;
    private Text projectPlaceOrRegionNameText;
    private Text projectLatitudeLongitudeText;
    private Text projectDatesOfCaptureText;
    private Text projectCitationInformationText;
    private Text projectRelatedPartyText;
    private Text projectRelatedActivityText;
    private Text projectRelatedInformationText;

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
        }, true, this.incomingFieldValues.getName());

        this.projectNameField.getControl().addKeyListener(this);

        final Label projectOwnerLabel = new Label(this.container, SWT.NULL);
        projectOwnerLabel.setText("Project Owner");

        this.projectOwnerText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectOwnerText.setText(this.incomingFieldValues.getOwner());
        this.projectOwnerText.addKeyListener(this);

        final Label projectDescriptionLabel = new Label(this.container, SWT.NULL);
        projectDescriptionLabel.setText("Project Description");

        this.projectDescriptionText = new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        this.projectDescriptionText.setText(this.incomingFieldValues.getDescription());
        this.projectDescriptionText.addKeyListener(this);

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
        this.projectCollectionDropDown.addKeyListener(this);

        final Label rightsStatementLabel = new Label(this.container, SWT.NULL);
        rightsStatementLabel.setText("Rights Statement");

        this.projectRightsStatementText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectRightsStatementText.setText(this.incomingFieldValues.getRightsStatement());
        this.projectRightsStatementText.addKeyListener(this);

        final Label accessRightsLabel = new Label(this.container, SWT.NULL);
        accessRightsLabel.setText("Access Rights");

        this.projectAccessRightsText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectAccessRightsText.setText(this.incomingFieldValues.getAccessRights());
        this.projectAccessRightsText.addKeyListener(this);

        final Label licenceLabel = new Label(this.container, SWT.NULL);
        licenceLabel.setText("Licence");

        this.projectLicenceText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectLicenceText.setText(this.incomingFieldValues.getLicence());
        this.projectLicenceText.addKeyListener(this);

        final Label identifierLabel = new Label(this.container, SWT.NULL);
        identifierLabel.setText("Identifier");

        this.projectIdentifierText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectIdentifierText.setText(this.incomingFieldValues.getIdentifier());
        this.projectIdentifierText.addKeyListener(this);

        final Label subjectLabel = new Label(this.container, SWT.NULL);
        subjectLabel.setText("Subject");

        this.projectSubjectText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectSubjectText.setText(this.incomingFieldValues.getSubject());
        this.projectSubjectText.addKeyListener(this);

        final Label electronicLocationLabel = new Label(this.container, SWT.NULL);
        electronicLocationLabel.setText("Electronic Location");

        this.projectElectronicLocationText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectElectronicLocationText.setText(this.incomingFieldValues.getElectronicLocation());
        this.projectElectronicLocationText.addKeyListener(this);

        final Label physicalLocationLabel = new Label(this.container, SWT.NULL);
        physicalLocationLabel.setText("Physical Location");

        this.projectPhysicalLocationText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectPhysicalLocationText.setText(this.incomingFieldValues.getPhysicalLocation());
        this.projectPhysicalLocationText.addKeyListener(this);

        final Label placeOrRegionNameLabel = new Label(this.container, SWT.NULL);
        placeOrRegionNameLabel.setText("Place or Region Name");

        this.projectPlaceOrRegionNameText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectPlaceOrRegionNameText.setText(this.incomingFieldValues.getPlaceOrRegionName());
        this.projectPlaceOrRegionNameText.addKeyListener(this);

        final Label latitudeLongitudeLabel = new Label(this.container, SWT.NULL);
        latitudeLongitudeLabel.setText("Latitude/Longitude");

        this.projectLatitudeLongitudeText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectLatitudeLongitudeText.setText(this.incomingFieldValues.getLatitudeLongitude());
        this.projectLatitudeLongitudeText.addKeyListener(this);

        final Label datesOfCaptureLabel = new Label(this.container, SWT.NULL);
        datesOfCaptureLabel.setText("Dates of Capture");

        this.projectDatesOfCaptureText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectDatesOfCaptureText.setText(this.incomingFieldValues.getDatesOfCapture());
        this.projectDatesOfCaptureText.addKeyListener(this);

        final Label citationInformationLabel = new Label(this.container, SWT.NULL);
        citationInformationLabel.setText("Citation Information");

        this.projectCitationInformationText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectCitationInformationText.setText(this.incomingFieldValues.getCitationInformation());
        this.projectCitationInformationText.addKeyListener(this);

        final Label relatedPartyLabel = new Label(this.container, SWT.NULL);
        relatedPartyLabel.setText("Related Party");

        this.projectRelatedPartyText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectRelatedPartyText.setText(this.incomingFieldValues.getRelatedParty());
        this.projectRelatedPartyText.addKeyListener(this);

        final Label relatedActivityLabel = new Label(this.container, SWT.NULL);
        relatedActivityLabel.setText("Related Activity");

        this.projectRelatedActivityText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectRelatedActivityText.setText(this.incomingFieldValues.getRelatedActivity());
        this.projectRelatedActivityText.addKeyListener(this);

        final Label relatedInformationLabel = new Label(this.container, SWT.NULL);
        relatedInformationLabel.setText("Related Information");

        this.projectRelatedInformationText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectRelatedInformationText.setText(this.incomingFieldValues.getRelatedInformation());
        this.projectRelatedInformationText.addKeyListener(this);

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.projectNameField.getControl().setLayoutData(singleLineGridData);
        this.projectOwnerText.setLayoutData(singleLineGridData);
        this.projectDescriptionText.setLayoutData(multiLineGridData);
        this.projectCollectionDropDown.setLayoutData(singleLineGridData);
        this.projectRightsStatementText.setLayoutData(singleLineGridData);
        this.projectAccessRightsText.setLayoutData(singleLineGridData);
        this.projectLicenceText.setLayoutData(singleLineGridData);
        this.projectIdentifierText.setLayoutData(singleLineGridData);
        this.projectSubjectText.setLayoutData(singleLineGridData);
        this.projectElectronicLocationText.setLayoutData(singleLineGridData);
        this.projectPhysicalLocationText.setLayoutData(singleLineGridData);
        this.projectPlaceOrRegionNameText.setLayoutData(singleLineGridData);
        this.projectLatitudeLongitudeText.setLayoutData(singleLineGridData);
        this.projectDatesOfCaptureText.setLayoutData(singleLineGridData);
        this.projectCitationInformationText.setLayoutData(singleLineGridData);
        this.projectRelatedPartyText.setLayoutData(singleLineGridData);
        this.projectRelatedActivityText.setLayoutData(singleLineGridData);
        this.projectRelatedInformationText.setLayoutData(singleLineGridData);

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

    public ProjectFieldsDTO getProjectFields()
    {
        return new ProjectFieldsDTO(this.projectNameField.getContents().trim(), this.projectOwnerText.getText().trim(),
                this.projectDescriptionText.getText().trim(),
                this.projectCollectionDropDown.getItem(this.projectCollectionDropDown.getSelectionIndex()),
                this.projectRightsStatementText.getText().trim(), this.projectAccessRightsText.getText().trim(),
                this.projectLicenceText.getText().trim(), this.projectIdentifierText.getText().trim(),
                this.projectSubjectText.getText().trim(), this.projectElectronicLocationText.getText().trim(),
                this.projectPhysicalLocationText.getText().trim(), this.projectPlaceOrRegionNameText.getText().trim(),
                this.projectLatitudeLongitudeText.getText().trim(), this.projectDatesOfCaptureText.getText().trim(),
                this.projectCitationInformationText.getText().trim(), this.projectRelatedPartyText.getText().trim(),
                this.projectRelatedActivityText.getText().trim(), this.projectRelatedInformationText.getText().trim());
    }
}
