/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.domain.Project;

/**
 * 
 */
public final class ProjectManager implements IProjectManager
{

    private Project currentProject;

    public ProjectManager()
    {
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public synchronized void setCurrentProject(final Project project)
    {
        this.currentProject = project;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public synchronized Project getCurrentProject()
    {
        return this.currentProject;
    }

}
