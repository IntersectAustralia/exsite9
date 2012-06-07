/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import static org.junit.Assert.*;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Project;

/**
 * Tests {@link ProjectManager}
 */
public final class ProjectManagerUnitTest
{
    @Test
    public void testProjectManager()
    {
        final Project project1 = new Project();
        final Project project2 = new Project("proj2", "own", "desc");

        final ProjectManager toTest = new ProjectManager();

        assertNull(toTest.getCurrentProject());
        toTest.setCurrentProject(project1);
        assertSame(project1, toTest.getCurrentProject());
        toTest.setCurrentProject(project2);
        assertSame(project2, toTest.getCurrentProject());
    }
}
