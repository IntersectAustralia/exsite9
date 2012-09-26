/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.openproject;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.service.IProjectService;

/**
 * Wizard used to open an existing {@link Project}, the user is shown the wizard page {@link OpenProjectWizardPage1}
 */
public class OpenProjectWizard extends Wizard
{
    private OpenProjectWizardPage1 page1;
    final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class);

    private Project selectedProject;

    /**
     * Constructor 
     */
    public OpenProjectWizard()
    {
        super();
        setNeedsProgressMonitor(true);
        setWindowTitle("Open Project");
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addPages()
    {
        final List<Project> availableProjects = projectService.getAllProjects();
        page1 = new OpenProjectWizardPage1(availableProjects);
        addPage(this.page1);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean performFinish()
    {
        this.selectedProject = page1.getSelectedProject();
        return this.selectedProject != null;
    }

    public Project getSelectedProject()
    {
        return this.selectedProject;
    }
}
