/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.newproject;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectService;

/**
 * The Wizard used to create a new project
 */
public final class NewProjectWizard extends Wizard
{
    private final NewProjectWizardPage1 page1 = new NewProjectWizardPage1();

    /**
     * Constructor 
     */
    public NewProjectWizard()
    {
        super();
        setNeedsProgressMonitor(true);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addPages()
    {
        addPage(this.page1);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        final String projectName = page1.getProjectName();
        final String projectOwner = page1.getProjectOwner();
        final String projectDescription = page1.getProjectDescription();

        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);
        final Project project = projectService.createProject(projectName, projectOwner, projectDescription);
        return project != null;
    }
}
