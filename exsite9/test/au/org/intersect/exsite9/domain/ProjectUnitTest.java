/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.assertNotEquals;
import static au.org.intersect.exsite9.test.Assert.assertNotEqualsHashCode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link Project}
 */
public final class ProjectUnitTest
{

    private static final String EMPTY_STRING = "";

    @Test
    public void testConstruction()
    {

        ProjectFieldsDTO projectFields = new ProjectFieldsDTO("project 1", "owner 1", "Project One", EMPTY_STRING,
                EMPTY_STRING, EMPTY_STRING, "to kill", EMPTY_STRING, "random", EMPTY_STRING, EMPTY_STRING, "Sydney",
                EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, "Intersect", EMPTY_STRING, EMPTY_STRING);

        final Project toTest1 = new Project(projectFields);

        assertEquals(projectFields.getName(), toTest1.getName());
        assertTrue(toTest1.getRootNode().getGroups().size() == 1);
        assertTrue(toTest1.getRootNode().getResearchFiles().isEmpty());
        assertEquals(projectFields.getDescription(), toTest1.getDescription());
        assertEquals(projectFields.getOwner(), toTest1.getOwner());
        assertEquals(projectFields.getLicence(), toTest1.getLicence());
        assertEquals(projectFields.getRelatedParty(), toTest1.getRelatedParty());
        assertEquals(projectFields.getRelatedInformation(), toTest1.getRelatedInformation());
        assertTrue(toTest1.getMetadataCategories().isEmpty());

        final String toString = toTest1.toString();

        assertTrue(toString.contains("name=project 1"));
        assertTrue(toString.contains("description=Project One"));
        assertTrue(toString.contains("owner=owner 1"));
        assertTrue(toString.contains("licence=to kill"));
        assertTrue(toString.contains("place or region name=Sydney"));
        assertTrue(toString.contains("identifier="));

        toTest1.setFolders(null);
        toTest1.setMetadataCategories(null);
        assertNull(toTest1.getFolders());
        assertNull(toTest1.getMetadataCategories());
    }

    @Test
    public void testEqualsHashCode()
    {
        final String n1 = "project 1";
        final String o1 = "owner 1";
        final String d1 = "Project One";
        final String n2 = "project 2";
        final String o2 = "owner 2";
        final String d2 = "Project Two";
        
        ProjectFieldsDTO proj1 = new ProjectFieldsDTO(n1, o1, d1, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);
        ProjectFieldsDTO proj2 = new ProjectFieldsDTO(n2, o2, d2, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING);

        
        final Project toTest1 = new Project(proj1);
        toTest1.setId(7l);
        final Project toTest2 = new Project(proj1);
        toTest2.setId(7l);
        final Project toTest3 = new Project(proj2);

        assertEquals(toTest1, toTest1);

        assertEquals(toTest1, toTest2);
        assertEquals(toTest2, toTest1);
        assertEquals(toTest1.hashCode(), toTest2.hashCode());

        // Different name
        assertNotEqualsHashCode(toTest1, toTest3);
        assertNotEqualsHashCode(toTest2, toTest3);

        // Different child nodes.
        toTest1.getRootNode().getGroups().add(new Group("some group"));
        assertEquals(toTest1, toTest2);
        assertNotEquals(toTest1, toTest3);

        // Different child files.
        toTest1.getRootNode().getResearchFiles().add(new ResearchFile(new File("some File")));
        assertEquals(toTest1, toTest2);
        assertNotEquals(toTest1, toTest3);

        assertNotEquals(toTest1, null);
        assertNotEquals(toTest1, new Object());
        assertNotEquals(toTest1, n1);
    }
}
