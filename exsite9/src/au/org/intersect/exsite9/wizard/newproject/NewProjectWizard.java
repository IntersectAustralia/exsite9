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
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;
import au.org.intersect.exsite9.service.IProjectService;

/**
 * The Wizard used to create a new project
 */
public final class NewProjectWizard extends Wizard
{
    private static String EMPTY_STRING = "";
    private final EditOrCreateProjectWizardPage1 page1;

    private Project newProject;

    /**
     * Constructor
     */
    public NewProjectWizard()
    {
        super();
        setNeedsProgressMonitor(true);
        page1 = new EditOrCreateProjectWizardPage1("New Project", "Please enter the details of your new project.",
                new ProjectFieldsDTO(EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
                        EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
                        EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
                        EMPTY_STRING));
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void addPages()
    {
        addPage(this.page1);
    }

    /**
     * @{inheritDoc
     */
    @Override
    public boolean performFinish()
    {
        final IProjectService projectService = (IProjectService) PlatformUI.getWorkbench().getService(
                IProjectService.class);
        this.newProject = projectService.createProject(page1.getProjectFields(), "", "", "", true);

        return this.newProject != null;
    }

    public Project getNewProject()
    {
        return this.newProject;
    }
}
