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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The first page of the new project wizard.
 */
public final class NewProjectWizardPage1 extends WizardPage implements KeyListener
{
    private Text projectNameText;
    private Text projectDescriptionText;
    private Text projectOwnerText;

    private Composite container;

    /**
     * Constructor
     */
    public NewProjectWizardPage1()
    {
        super("New Project");
        setTitle("New Project");
        setDescription("Please enter the details of your new project.");
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;

        final Label projectNameLabel = new Label(this.container, SWT.NULL);
        projectNameLabel.setText("Project Name");

        this.projectNameText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectNameText.setText("");
        this.projectNameText.addKeyListener(this);

        final Label projectOwnerLabel = new Label(this.container, SWT.NULL);
        projectOwnerLabel.setText("Project Owner");

        this.projectOwnerText = new Text(this.container, SWT.BORDER | SWT.SINGLE);
        this.projectOwnerText.setText("");
        this.projectOwnerText.addKeyListener(this);

        final Label projectDescriptionLabel = new Label(this.container, SWT.NULL);
        projectDescriptionLabel.setText("Project Description");

        this.projectDescriptionText = new Text(this.container, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        this.projectDescriptionText.setText("");
        this.projectDescriptionText.addKeyListener(this);

        final GridData singleLineGridData = new GridData(GridData.FILL_HORIZONTAL);
        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.projectNameText.setLayoutData(singleLineGridData);
        this.projectOwnerText.setLayoutData(singleLineGridData);
        this.projectDescriptionText.setLayoutData(multiLineGridData);

        setControl(this.container);
        setPageComplete(false);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void keyPressed(final KeyEvent e)
    {
        final boolean pageCompleted = !this.projectNameText.getText().isEmpty() && !this.projectOwnerText.getText().isEmpty();
        setPageComplete(pageCompleted);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent e)
    {
    }

    public String getProjectName()
    {
        return this.projectNameText.getText().trim();
    }

    public String getProjectOwner()
    {
        return this.projectOwnerText.getText().trim();
    }

    public String getProjectDescription()
    {
        return this.projectDescriptionText.getText().trim();
    }
}
