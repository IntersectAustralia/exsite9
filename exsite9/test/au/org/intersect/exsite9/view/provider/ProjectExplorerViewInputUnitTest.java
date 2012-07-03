/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import static org.junit.Assert.*;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Project;

/**
 * Tests {@link ProjectViewInputWrapper}
 */
public final class ProjectExplorerViewInputUnitTest
{

    @Test
    public void testConstruction()
    {
        final Project project = new Project("some project name","owner","description");
        final ProjectViewInputWrapper toTest = new ProjectViewInputWrapper(project);
        assertEquals(project, toTest.getProject());
    }
}
