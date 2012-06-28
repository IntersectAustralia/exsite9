/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import org.eclipse.ui.PlatformUI;

import au.org.intersect.exsite9.domain.Project;

/**
 * Provides the ability to control the currently active project.
 */
public final class ProjectManager implements IProjectManager
{
    /**
     * The ID of the current project loaded in the UI.
     */
    private Long currentProjectID;

    private IProjectService projectService;

    public ProjectManager()
    {
        this((IProjectService) PlatformUI.getWorkbench().getService(IProjectService.class));
    }

    ProjectManager(final IProjectService projectService)
    {
        this.projectService = projectService;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public synchronized void setCurrentProjectID(final Long projectID)
    {
        this.currentProjectID = projectID;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public synchronized Project getCurrentProject()
    {
        if (this.currentProjectID == null)
        {
            return null;
        }
        return this.projectService.findProjectById(this.currentProjectID);
    }

}
