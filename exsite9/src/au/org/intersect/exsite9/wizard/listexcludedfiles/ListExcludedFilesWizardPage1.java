/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.listexcludedfiles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Excluded Files wizard page 1.
 */
public final class ListExcludedFilesWizardPage1 extends WizardPage implements SelectionListener
{
    private final List<ResearchFile> excludedFiles;

    private final List<ResearchFile> excludedFilesToInclude = new ArrayList<ResearchFile>();

    private org.eclipse.swt.widgets.List excludedFilesList;

    /**
     * Constructor
     * @param excludedFiles The list of currently excluded files.
     */
    public ListExcludedFilesWizardPage1(final List<ResearchFile> excludedFiles)
    {
        super("Excluded Files");
        setTitle("Excluded Files");
        setDescription("These are files that have been excluded from the project.");
        this.excludedFiles = excludedFiles;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createControl(final Composite parent)
    {
        final Composite container = new Composite(parent, SWT.NULL);
        final GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        container.setLayout(layout);
        
        this.excludedFilesList = new org.eclipse.swt.widgets.List(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP | SWT.V_SCROLL);

        for (final ResearchFile rf : this.excludedFiles)
        {
            this.excludedFilesList.add(rf.getFile().getAbsolutePath());
        }

        final GridData multiLineGridData = new GridData(GridData.FILL_BOTH);
        this.excludedFilesList.setLayoutData(multiLineGridData);

        Composite rowComp = new Composite(container, SWT.NULL);

        final RowLayout rowLayout = new RowLayout();
        rowLayout.type = SWT.VERTICAL;
        rowLayout.pack = false;
        rowLayout.justify = true;
        rowComp.setLayout(rowLayout);

        final Button removeButton = new Button(rowComp, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.addSelectionListener(this);
        removeButton.setEnabled(false);

        this.excludedFilesList.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(final SelectionEvent e)
            {
                removeButton.setEnabled(excludedFilesList.getSelectionCount() > 0);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e)
            {
            }
        });

        setControl(container);
        setPageComplete(false);
    }

    @Override
    public void widgetSelected(final SelectionEvent event)
    {
        if (this.excludedFilesList.getSelectionCount() == 0)
        {
            return;
        }

        final int selectedIndex = this.excludedFilesList.getSelectionIndex();
        this.excludedFilesList.remove(selectedIndex);
        this.excludedFilesToInclude.add(this.excludedFiles.get(selectedIndex));
        this.setPageComplete(true);
    }

    @Override
    public void widgetDefaultSelected(final SelectionEvent event)
    {
    }

    public List<ResearchFile> getExcludedFilesToInclude()
    {
        return this.excludedFilesToInclude;
    }
}
