/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.provider;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Tests {@link ProjectExplorerViewContentProvider}
 */
public final class ProjectExplorerViewContentProviderUnitTest
{

    @Test
    public void testGetElements()
    {
        final ProjectExplorerViewContentProvider toTest = new ProjectExplorerViewContentProvider();
        assertArrayEquals(Collections.emptyList().toArray(), toTest.getElements(null));
        assertArrayEquals(Collections.emptyList().toArray(), toTest.getElements(new Object()));

        final Project project = new Project("[TM]","owner","description");
        final ProjectExplorerViewInput projectExplorerViewInput = new ProjectExplorerViewInput(project);

        final Object[] out = toTest.getElements(projectExplorerViewInput);
        assertEquals(1, out.length);
        assertEquals(project, out[0]);
    }

    @Test
    public void testGetChildren()
    {
        final ProjectExplorerViewContentProvider toTest = new ProjectExplorerViewContentProvider();
        assertArrayEquals(Collections.emptyList().toArray(), toTest.getChildren(null));
        assertArrayEquals(Collections.emptyList().toArray(), toTest.getChildren(new Object()));

        final Project project = new Project("SomeProject","owner","description");
        final Group group1 = new Group("group1");
        final Group group2 = new Group("group2");
        final ResearchFile rf1 = new ResearchFile("someFile");

        project.getRootNode().getGroups().add(group1);
        project.getRootNode().getGroups().add(group2);

        group1.getResearchFiles().add(rf1);

        final List<Object> out1 = Arrays.asList(toTest.getChildren(project.getRootNode()));
        // There will be 3 groups because a project gets a New Files group on creation
        assertEquals(3, out1.size());
        assertTrue(out1.contains(group1));
        assertTrue(out1.contains(group2));

        final List<Object> out2 = Arrays.asList(toTest.getChildren(group1));
        assertEquals(1, out2.size());
        assertTrue(out2.contains(rf1));

        final List<Object> out3 = Arrays.asList(toTest.getChildren(group2));
        assertTrue(out3.isEmpty());

        final List<Object> out4 = Arrays.asList(toTest.getChildren(rf1));
        assertTrue(out4.isEmpty());
    }
}
