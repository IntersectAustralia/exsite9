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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.FieldOfResearch;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.service.IFieldOfResearchService;
import au.org.intersect.exsite9.view.dialogs.ElementListSelectionDialog;

/**
 * Second page of the edit or create new project wizards.
 */
public final class EditOrCreateProjectWizardPage2 extends WizardPage
{
    private final IFieldOfResearchService fieldOfResearchService;

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

    private Composite container;

    private ProjectFieldsDTO incomingFieldValues;

    /**
     * Constructor
     */
    public EditOrCreateProjectWizardPage2(final String pageTitle, final String pageDescription, final ProjectFieldsDTO incomingFieldValues)
    {
        super(pageTitle);
        setTitle(pageTitle);
        setDescription(pageDescription);
        this.incomingFieldValues = incomingFieldValues;
        this.fieldOfResearchService = (IFieldOfResearchService) PlatformUI.getWorkbench().getService(IFieldOfResearchService.class);
    }

    @Override
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;

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
        setPageComplete(true);
    }

    public String getElectronicLocation()
    {
        return this.electronicLocationText.getText().trim();
    }

    public String getPhysicalLocation()
    {
        return this.physicalLocationText.getText().trim();
    }

    public String getPlaceOrRegionName()
    {
        return this.placeOrRegionNameText.getText().trim();
    }

    public String getGeographicalCoverage()
    {
        return this.geographicalCoverageText.getText().trim();
    }

    public String getDatesOfCapture()
    {
        return this.datesOfCaptureText.getText().trim();
    }

    public String getCitationInformation()
    {
        return this.citationInformationText.getText().trim();
    }

    public String getCountries()
    {
        return this.countriesText.getText().trim();
    }

    public String getLanguages()
    {
        return this.languagesText.getText().trim();
    }

    public FieldOfResearch getFieldOfResearch()
    {
        return this.fieldOfResearch;
    }

    public String getFundingBody()
    {
        return this.fundingBodyText.getText().trim();
    }

    public String getGrantID()
    {
        return this.grantIDText.getText().trim();
    }

    public String getRelatedParty()
    {
        return this.relatedPartyText.getText().trim();
    }

    public String getRelatedActivity()
    {
        return this.relatedActivityText.getText().trim();
    }

    public String getRelatedInformation()
    {
        return this.relatedInformationText.getText().trim();
    }
}
