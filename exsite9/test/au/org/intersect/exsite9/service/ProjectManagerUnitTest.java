/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link ProjectManager}
 */
public final class ProjectManagerUnitTest
{
    @Test
    public void testProjectManager()
    {
        final IProjectService projectService = Mockito.mock(IProjectService.class);

        final ProjectManager toTest = new ProjectManager(projectService);

        assertNull(toTest.getCurrentProject());

        final Long projectID = 99L;
        final Project project = new Project(new ProjectFieldsDTO("name", "owner", "institution", "email", "desc",
                "collectionType", "rightsStatement", "accessRights", "license", "identifier", "subject",
                "electronicLocation", "physicalLocation", "placeOrRegionName", "geoCoverage", "datesOfCapture",
                "citationInformation", "counries", "languages", null, "fundingBody", "grantID", "relatedParty", "relatedGrant",
                "relatedInformation"));

        project.setId(projectID);

        toTest.setCurrentProjectID(projectID);

        when(projectService.findProjectById(projectID)).thenReturn(project);
        assertEquals(project, toTest.getCurrentProject());
    }
}
