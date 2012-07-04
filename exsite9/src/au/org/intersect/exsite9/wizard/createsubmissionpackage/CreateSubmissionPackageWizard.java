/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.createsubmissionpackage;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.service.IProjectManager;
import au.org.intersect.exsite9.service.ISubmissionPackageService;

/**
 * A wizard that can be used to create a submission package.
 */
public final class CreateSubmissionPackageWizard extends Wizard
{
    private final CreateSubmissionPackageWizardPage1 page1;
    private final CreateSubmissionPackageWizardPage2 page2;

    private SubmissionPackage submissionPackage;

    /**
     * Constructor
     * @param submissionPackage The submission package to fill in the dialogs with. May be {@code null} in which case they will be blank.
     */
    public CreateSubmissionPackageWizard(final SubmissionPackage submissionPackage, final Collection<SubmissionPackage> existingSubmissionPackages, final String wizardTitle)
    {
        super();
        setNeedsProgressMonitor(true);
        this.submissionPackage = submissionPackage;
        this.page1 = new CreateSubmissionPackageWizardPage1(submissionPackage, existingSubmissionPackages, wizardTitle);
        this.page2 = new CreateSubmissionPackageWizardPage2(submissionPackage, wizardTitle);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addPages()
    {
        addPage(this.page1);
        addPage(this.page2);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        final String name = page1.getSubmissionPackageName();
        final String description = page1.getSubmissionPackageDescription();
        final List<ResearchFile> researchFiles = page2.getCheckedResearchFiles();

        final IProjectManager projectManager = (IProjectManager) PlatformUI.getWorkbench().getService(IProjectManager.class);
        final Project currentProject = projectManager.getCurrentProject();

        final ISubmissionPackageService submissionPackageService = (ISubmissionPackageService) PlatformUI.getWorkbench().getService(ISubmissionPackageService.class);

        if (this.submissionPackage == null)
        {
            this.submissionPackage = submissionPackageService.createSubmissionPackage(currentProject, name, description, researchFiles);
        }
        else
        {
            this.submissionPackage = submissionPackageService.updateSubmissionPackage(this.submissionPackage, name, description, researchFiles);
        }

        return this.submissionPackage != null;
    }

    public SubmissionPackage getSubmissionPackage()
    {
        return this.submissionPackage;
    }
}
