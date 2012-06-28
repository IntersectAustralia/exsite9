/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.domain.Project;

/**
 * Manages the current project being worked on.
 */
public interface IProjectManager
{
    /**
     * Sets the currently active project.
     * @param project The currently active project.
     */
    public void setCurrentProjectID(final Long projectID);

    /**
     * Obtains the currently active project. May be {@code null} if there is no active project.
     * @return The currently active project.
     */
    public Project getCurrentProject();
}
