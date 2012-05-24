/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import au.org.intersect.exsite9.domain.Project;

/**
 * Wraps the root node of the project explorer view.
 */
public final class ProjectExplorerViewInput
{
    private final Project project;

    public ProjectExplorerViewInput(final Project project)
    {
        this.project = project;
    }

    public Project getProject()
    {
        return this.project;
    }
}
