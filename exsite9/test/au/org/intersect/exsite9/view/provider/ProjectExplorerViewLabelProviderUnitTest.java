/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import static org.junit.Assert.*;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Tests {@link ProjectExplorerViewLabelProvider}
 */
public final class ProjectExplorerViewLabelProviderUnitTest
{

    @Test
    public void testGetText()
    {
        final ProjectExplorerViewLabelProvider toTest = new ProjectExplorerViewLabelProvider();

        try
        {
            toTest.getText("SomeString");
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            // Expected.
        }

        try
        {
            toTest.getText(null);
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            // Expected.
        }

        try
        {
            toTest.getText(new Object());
            fail();
        }
        catch (final IllegalArgumentException e)
        {
            // Expected.
        }

        final String projectName = "projName";
        final String groupName = "groupName";
        final String fileName = "fName";
        final Project project = new Project(projectName);
        final Group group = new Group(groupName);
        final ResearchFile rf = new ResearchFile(fileName);

        assertEquals(projectName, toTest.getText(project));
        assertEquals(groupName, toTest.getText(group));
        assertEquals(fileName, toTest.getText(rf));
    }
}
