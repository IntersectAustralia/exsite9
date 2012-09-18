/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newproject;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.richclientgui.toolbox.validation.IFieldErrorMessageHandler;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;

import au.org.intersect.exsite9.domain.FieldOfResearch;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.service.IFieldOfResearchService;
import au.org.intersect.exsite9.view.dialogs.ElementListSelectionDialog;
import au.org.intersect.exsite9.wizard.MaximumLengthFieldValidator;
import au.org.intersect.exsite9.wizard.TrueFieldValidator;
import au.org.intersect.exsite9.wizard.WizardPageErrorHandler;

/**
 * The first page of the new project wizard.
 */
public final class EditOrCreateProjectWizardPage1 extends WizardPage implements KeyListener
{
    private ValidatingField<String> projectNameText;
    private ValidatingField<String> ownerText;
    private Text institutionText;
    private ValidatingField<String> emailText;
    private Text descriptionText;
    private Combo collectionDropDown;
    private Text rightsStatementText;
    private Text accessRightsText;
    private Text licenceText;
    private Text identifierText;
    private Text subjectText;
    private Text electronicLocationText;
    private Text physicalLocationText;
    private Text placeOrRegionNameText;
    private Text geographicalCoverageText;
    private Text datesOfCaptureText;
    private Text citationInformationText;
    private Text countriesText;
    private Text languagesText;
    private FieldOfResearch fieldOfResearch;
    private Text fundingBodyText;
    private Text grantIDText;
    private Text relatedPartyText;
    private Text relatedActivityText;
    private Text relatedInformationText;

    private ProjectFieldsDTO incomingFieldValues;

    private StringValidationToolkit stringValidatorToolkit;
    private final IFieldErrorMessageHandler errorMessageHandler = new WizardPageErrorHandler(this);

    private Composite container;

    private final IFieldOfResearchService fieldOfResearchService;

    /**
     * Constructor
     */
    public EditOrCreateProjectWizardPage1(final String pageTitle, final String pageDescription, final ProjectFieldsDTO incomingFieldValues)
    {
        super(pageTitle);
        setTitle(pageTitle);
        setDescription(pageDescription);
        this.incomingFieldValues = incomingFieldValues;
        this.fieldOfResearchService = (IFieldOfResearchService) PlatformUI.getWorkbench().getService(IFieldOfResearchService.class);
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

        final Label identifierLabel = new Label(this.container, SWT.NULL);
        identifierLabel.setText("Identifier");

        this.identifierText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.identifierText.setText(this.incomingFieldValues.getIdentifier());

        final Label projectNameLabel = new Label(this.container, SWT.NULL);
        projectNameLabel.setText("Project Name");

        this.projectNameText = this.stringValidatorToolkit.createTextField(this.container, new MaximumLengthFieldValidator("Project Name", 255),
                true, this.incomingFieldValues.getName());
        this.projectNameText.getControl().addKeyListener(this);

        final Label nameLabel = new Label(this.container, SWT.NULL);
        nameLabel.setText("Name");

        this.ownerText = this.stringValidatorToolkit.createTextField(this.container, new TrueFieldValidator(), true, this.incomingFieldValues.getOwner());
        this.ownerText.getControl().addKeyListener(this);

        final Label institutionLabel = new Label(this.container, SWT.NULL);
        institutionLabel.setText("Institution");

        this.institutionText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.institutionText.setText(this.incomingFieldValues.getInstitution());

        final Label emailLabel = new Label(this.container, SWT.NULL);
        emailLabel.setText("Email");

        this.emailText = this.stringValidatorToolkit.createTextField(this.container, new TrueFieldValidator(), true, this.incomingFieldValues.getEmail());
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

        final Label electronicLocationLabel = new Label(this.container, SWT.NULL);
        electronicLocationLabel.setText("Electronic Location");

        this.electronicLocationText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.electronicLocationText.setText(this.incomingFieldValues.getElectronicLocation());

        final Label physicalLocationLabel = new Label(this.container, SWT.NULL);
        physicalLocationLabel.setText("Physical Location");

        this.physicalLocationText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.physicalLocationText.setText(this.incomingFieldValues.getPhysicalLocation());

        final Label placeOrRegionNameLabel = new Label(this.container, SWT.NULL);
        placeOrRegionNameLabel.setText("Place or Region Name");

        this.placeOrRegionNameText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.placeOrRegionNameText.setText(this.incomingFieldValues.getPlaceOrRegionName());

        final Label latitudeLongitudeLabel = new Label(this.container, SWT.NULL);
        latitudeLongitudeLabel.setText("Geographical Coverage");

        this.geographicalCoverageText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.geographicalCoverageText.setText(this.incomingFieldValues.getGeographicalCoverage());

        final Label datesOfCaptureLabel = new Label(this.container, SWT.NULL);
        datesOfCaptureLabel.setText("Dates of Capture");

        this.datesOfCaptureText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.datesOfCaptureText.setText(this.incomingFieldValues.getDatesOfCapture());

        final Label citationInformationLabel = new Label(this.container, SWT.NULL);
        citationInformationLabel.setText("Citation Information");

        this.citationInformationText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.citationInformationText.setText(this.incomingFieldValues.getCitationInformation());

        final Label countriesLabel = new Label(this.container, SWT.NULL);
        countriesLabel.setText("Countries");

        this.countriesText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.countriesText.setText(this.incomingFieldValues.getCountries());

        final Label languagesLabel = new Label(this.container, SWT.NULL);
        languagesLabel.setText("Languages");

        this.languagesText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.languagesText.setText(this.incomingFieldValues.getLanguages());
        
        final Label fieldOfResearchLabel = new Label(this.container, SWT.NULL);
        fieldOfResearchLabel.setText("Field of Research");

        final Text fieldOfResearchText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        fieldOfResearchText.setEditable(false);
        fieldOfResearchText.setEnabled(false);
        this.fieldOfResearch = this.incomingFieldValues.getFieldOfResearch();
        if (this.fieldOfResearch != null)
        {
            fieldOfResearchText.setText(this.fieldOfResearch.toString());
        }

        new Label(this.container, SWT.NULL);

        final Button browseButton = new Button(this.container, SWT.PUSH);
        browseButton.setText("Select...");
        browseButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

        browseButton.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                final ElementListSelectionDialog selectDialog = new ElementListSelectionDialog(getShell(), new LabelProvider());
                final List<FieldOfResearch> fieldsOfResearch = fieldOfResearchService.getAll();
                selectDialog.setElements(fieldsOfResearch.toArray());
                final int response = selectDialog.open();
                if (response != Window.OK)
                {
                    return;
                }
                fieldOfResearch = (FieldOfResearch) selectDialog.getFirstResult();
                fieldOfResearchText.setText(fieldOfResearch.toString());
            }
            
            @Override
            public void widgetDefaultSelected(final SelectionEvent e)
            {
            }
        });

        final Label fundingBodyLabel = new Label(this.container, SWT.NULL);
        fundingBodyLabel.setText("Funding Body");

        this.fundingBodyText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.fundingBodyText.setText(this.incomingFieldValues.getFundingBody());
        
        final Label grantIDLabel = new Label(this.container, SWT.NULL);
        grantIDLabel.setText("Grant ID");

        this.grantIDText = new Text(this.container, SWT.NULL | SWT.BORDER);
        grantIDText.setText(this.incomingFieldValues.getGrantID());

        final Label relatedPartyLabel = new Label(this.container, SWT.NULL);
        relatedPartyLabel.setText("Related Party");

        this.relatedPartyText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.relatedPartyText.setText(this.incomingFieldValues.getRelatedParty());

        final Label relatedActivityLabel = new Label(this.container, SWT.NULL);
        relatedActivityLabel.setText("Related Grant");

        this.relatedActivityText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.relatedActivityText.setText(this.incomingFieldValues.getRelatedGrant());

        final Label relatedInformationLabel = new Label(this.container, SWT.NULL);
        relatedInformationLabel.setText("Related Information");

        this.relatedInformationText = new Text(this.container, SWT.SINGLE | SWT.BORDER);
        this.relatedInformationText.setText(this.incomingFieldValues.getRelatedInformation());

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
        this.electronicLocationText.setLayoutData(singleLineGridData);
        this.physicalLocationText.setLayoutData(singleLineGridData);
        this.placeOrRegionNameText.setLayoutData(singleLineGridData);
        this.geographicalCoverageText.setLayoutData(singleLineGridData);
        this.datesOfCaptureText.setLayoutData(singleLineGridData);
        this.citationInformationText.setLayoutData(singleLineGridData);
        this.countriesText.setLayoutData(singleLineGridData);
        this.languagesText.setLayoutData(singleLineGridData);
        fieldOfResearchText.setLayoutData(singleLineGridData);
        this.fundingBodyText.setLayoutData(singleLineGridData);
        this.grantIDText.setLayoutData(singleLineGridData);
        this.relatedPartyText.setLayoutData(singleLineGridData);
        this.relatedActivityText.setLayoutData(singleLineGridData);
        this.relatedInformationText.setLayoutData(singleLineGridData);

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

    public ProjectFieldsDTO getProjectFields()
    {
        return new ProjectFieldsDTO(this.projectNameText.getContents().trim(),
                this.ownerText.getContents().trim(), this.institutionText.getText().trim(),
                this.emailText.getContents().trim(), this.descriptionText.getText().trim(),                
                this.collectionDropDown.getItem(this.collectionDropDown.getSelectionIndex()),
                this.rightsStatementText.getText().trim(), this.accessRightsText.getText().trim(),
                this.licenceText.getText().trim(), this.identifierText.getText().trim(),
                this.subjectText.getText().trim(), this.electronicLocationText.getText().trim(),
                this.physicalLocationText.getText().trim(), this.placeOrRegionNameText.getText().trim(),
                this.geographicalCoverageText.getText().trim(), this.datesOfCaptureText.getText().trim(),
                this.citationInformationText.getText().trim(), this.countriesText.getText().trim(),
                this.languagesText.getText().trim(), this.fieldOfResearch,
                this.fundingBodyText.getText().trim(), this.grantIDText.getText().trim(),
                this.relatedPartyText.getText().trim(), this.relatedActivityText.getText().trim(),
                this.relatedInformationText.getText().trim());
    }
    
    private boolean allFieldsAreValid()
    {
        return this.projectNameText.isValid() && !this.projectNameText.getContents().trim().isEmpty() &&
            this.ownerText.isValid() && !this.ownerText.getContents().trim().isEmpty() &&
            this.emailText.isValid() && !this.emailText.getContents().trim().isEmpty();
    }
}
