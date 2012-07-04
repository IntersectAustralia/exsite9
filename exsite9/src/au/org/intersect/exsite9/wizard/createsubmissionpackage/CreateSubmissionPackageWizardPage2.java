/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.createsubmissionpackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewContentProvider;
import au.org.intersect.exsite9.view.provider.ProjectExplorerViewLabelProvider;
import au.org.intersect.exsite9.view.provider.ProjectViewInputWrapper;

/**
 * The second page of the Create Submission Package Wizard.
 * Allows the user to configure the files/groups that are to be contained in the submission package.
 */
public final class CreateSubmissionPackageWizardPage2 extends WizardPage implements ICheckStateListener
{
    private ContainerCheckedTreeViewer treeViewer;
    private final SubmissionPackage currentSubmissionPackage;

    /**
     * Constructor
     */
    public CreateSubmissionPackageWizardPage2(final SubmissionPackage submissionPackage, final String pageTitle)
    {
        super(pageTitle);
        setTitle(pageTitle);
        setDescription("Choose the items from your project hierarchy to include in the submission package.");
        this.currentSubmissionPackage = submissionPackage;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void createControl(final Composite parent)
    {
        this.treeViewer = new ContainerCheckedTreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        this.treeViewer.setContentProvider(new ProjectExplorerViewContentProvider(false));
        this.treeViewer.setLabelProvider(new ProjectExplorerViewLabelProvider());
        this.treeViewer.addCheckStateListener(this);
        ColumnViewerToolTipSupport.enableFor(this.treeViewer);

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project currentProject = projectManager.getCurrentProject();
        final ProjectViewInputWrapper wrapper = new ProjectViewInputWrapper(currentProject);

        this.treeViewer.setInput(wrapper);
        this.treeViewer.expandAll();

        if (this.currentSubmissionPackage != null)
        {
            for (final ResearchFile researchFile : this.currentSubmissionPackage.getResearchFiles())
            {
                this.treeViewer.setChecked(researchFile, true);
            }
        }

        setControl(this.treeViewer.getControl());
        setPageComplete(true);
    }

    @Override
    public void checkStateChanged(final CheckStateChangedEvent event)
    {
    }

    public List<ResearchFile> getCheckedResearchFiles()
    {
        final List<Object> checkedItems = Arrays.asList(this.treeViewer.getCheckedElements());
        final List<ResearchFile> toReturn = new ArrayList<ResearchFile>();

        for (final Object item : checkedItems)
        {
            if (item instanceof ResearchFile)
            {
                toReturn.add((ResearchFile)item);
            }
        }

        return toReturn;
    }
}
