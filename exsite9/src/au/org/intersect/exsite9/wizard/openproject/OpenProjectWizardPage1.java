/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.openproject;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import au.org.intersect.exsite9.domain.Project;

/**
 * Wizard page that is shown to the user when opening an existing {@link Project}
 */
public class OpenProjectWizardPage1 extends WizardPage implements SelectionListener
{
    private org.eclipse.swt.widgets.List projectList;

    private Composite container;

    private List<Project> availableProjects;

    private Project selectedProject;

    /**
     * Constructor
     */
    public OpenProjectWizardPage1(final List<Project> availableProjects)
    {
        super("Open Project");
        setTitle("Open Project");
        setDescription("Please choose the project you would like to open.");
        this.availableProjects = availableProjects;
    }

    /**
     * @{inheritDoc
     */
    public void createControl(final Composite parent)
    {
        this.container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
        layout.numColumns = 2;

        final Label selectProjectLabel = new Label(this.container, SWT.NULL);
        selectProjectLabel.setText("Select Project");

        this.projectList = new org.eclipse.swt.widgets.List(this.container, SWT.BORDER | SWT.SINGLE | SWT.WRAP | SWT.V_SCROLL);

        for (final Project project : this.availableProjects)
        {
            this.projectList.add(project.getName());
        }

        this.projectList.addSelectionListener(this);

        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);

        this.projectList.setLayoutData(multiLineGridData);

        setControl(this.container);
        setPageComplete(false);
    }

    @Override
    public void widgetSelected(final SelectionEvent e)
    {
        final int numSelected = this.projectList.getSelectionCount();
        if (numSelected == 0)
        {
            setPageComplete(false);
            return;
        }

        this.selectedProject = this.availableProjects.get(this.projectList.getSelectionIndex());
        setPageComplete(true);
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e)
    {
    }

    public Project getSelectedProject()
    {
        return this.selectedProject;
    }

}
