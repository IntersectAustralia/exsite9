/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import static au.org.intersect.exsite9.test.Assert.*;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link Project}
 */
public final class ProjectUnitTest
{

    @Test
    public void testConstruction()
    {

        ProjectFieldsDTO projectFields = new ProjectFieldsDTO("project 1", "owner 1", "Project One", "",
                "", "", "to kill", "", "random", "", "", "Sydney", "", "", "", "Intersect", "", "");

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
        assertFalse(toString.isEmpty());

        toTest1.setFolders(null);
        toTest1.setMetadataCategories(null);
        assertNull(toTest1.getFolders());
        assertNull(toTest1.getMetadataCategories());

        final String collectionType = "some collection type";
        toTest1.setCollectionType(collectionType);
        assertEquals(collectionType, toTest1.getCollectionType());

        final String rightsStatement = "miranda rights";
        toTest1.setRightsStatement(rightsStatement);
        assertEquals(rightsStatement, toTest1.getRightsStatement());

        final String accessRights = "ugo+777";
        toTest1.setAccessRights(accessRights);
        assertEquals(accessRights, toTest1.getAccessRights());

        final String licence = "007";
        toTest1.setLicence(licence);
        assertEquals(licence, toTest1.getLicence());

        final String identifier = "some identifier";
        toTest1.setIdentifier(identifier);
        assertEquals(identifier, toTest1.getIdentifier());

        final String subject = "the subject";
        toTest1.setSubject(subject);
        assertEquals(subject, toTest1.getSubject());

        final String electronicLocation = "some electronic location";
        toTest1.setElectronicLocation(electronicLocation);
        assertEquals(electronicLocation, toTest1.getElectronicLocation());

        final String physicalLocation = "vanuatu";
        toTest1.setPhysicalLocation(physicalLocation);
        assertEquals(physicalLocation, toTest1.getPhysicalLocation());

        final String placeOrRegionName = "some beach";
        toTest1.setPlaceOrRegionName(placeOrRegionName);
        assertEquals(placeOrRegionName, toTest1.getPlaceOrRegionName());

        final String latitudeLongitude = "latLong";
        toTest1.setLatitudeLongitude(latitudeLongitude);
        assertEquals(latitudeLongitude, toTest1.getLatitudeLongitude());

        final String datesOfCapture = "march 2012";
        toTest1.setDatesOfCapture(datesOfCapture);
        assertEquals(datesOfCapture, toTest1.getDatesOfCapture());

        final String citationInformation = "citation info";
        toTest1.setCitationInformation(citationInformation);
        assertEquals(citationInformation, toTest1.getCitationInformation());

        final String relatedParty = "a cool party";
        toTest1.setRelatedParty(relatedParty);
        assertEquals(relatedParty, toTest1.getRelatedParty());

        final String relatedActivity = "a related activity";
        toTest1.setRelatedActivity(relatedActivity);
        assertEquals(relatedActivity, toTest1.getRelatedActivity());

        final String relatedInformation = "a related piece of information";
        toTest1.setRelatedInformation(relatedInformation);
        assertEquals(relatedInformation, toTest1.getRelatedInformation());
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
        
        ProjectFieldsDTO proj1 = new ProjectFieldsDTO(n1, o1, d1, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        ProjectFieldsDTO proj2 = new ProjectFieldsDTO(n2, o2, d2, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

        
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
