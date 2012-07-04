/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.xml;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link ProjectXMLBuilderUnitTest}
 */
public final class ProjectXMLBuilderUnitTest
{
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String EMPTY_STRING = "";

    @Test
    public void testBuildProject()
    {
        final String projectName = "projectName";
        final String projectOwner = "project owner";
        final String projectDescription = "some boring description";
        final String projectAccessRights = "admin";
        final String projectCollectionType = "Dataset";
        final String projectDatesOfCapture = "next week";
        final String projectLatLong = "up and left a little";
        final String projectLicence = "to kill";
        final String projectPhysicalLocation = "Sydney";
        final String projectRelatedParty = "Intersect";
        final String projectSubject = "random";
        final Project project = new Project(new ProjectFieldsDTO(projectName, projectOwner, projectDescription,
                projectCollectionType, EMPTY_STRING, projectAccessRights, projectLicence, EMPTY_STRING, projectSubject,
                EMPTY_STRING, projectPhysicalLocation, EMPTY_STRING, projectLatLong, projectDatesOfCapture,
                EMPTY_STRING, projectRelatedParty, EMPTY_STRING, EMPTY_STRING));
        final MetadataCategory mdc1 = new MetadataCategory("CategoryName");
        final MetadataValue mdv11 = new MetadataValue("val11");
        final MetadataValue mdv12 = new MetadataValue("val12");
        final MetadataValue mdv13 = new MetadataValue("val13");
        final MetadataAssociation mda1 = new MetadataAssociation(mdc1);
        mda1.getMetadataValues().addAll(Arrays.asList(mdv11, mdv12, mdv13));

        final File file1 = new File("someResearchFile.txt");
        final File file2 = new File("someOtherResearchFile.txt");
        final ResearchFile rf1 = new ResearchFile(file1);
        final ResearchFile rf2 = new ResearchFile(file2);

        final Group group1 = new Group("group1");
        group1.getMetadataAssociations().add(mda1);
        group1.getResearchFiles().add(rf1);

        project.getRootNode().getGroups().add(group1);
        project.getRootNode().getResearchFiles().add(rf2);

        final String xml = ProjectXMLBuilder.buildXML(project);

        final String expectedXML =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE +
                "<project accessRights=\"admin\" citationInformation=\"\" collectionType=\"Dataset\" datesOfCapture=\"next week\" description=\"some boring description\" electronicLocation=\"\" identifier=\"\" latitudeLongitude=\"up and left a little\" licence=\"to kill\" name=\"projectName\" owner=\"project owner\" physicalLocation=\"Sydney\" placeOrRegionName=\"\" relatedActivity=\"\" relatedInformation=\"\" relatedParty=\"Intersect\" rightsStatement=\"\" subject=\"random\">" + NEW_LINE +
                                    "  <group name=\"New Files\"/>" + NEW_LINE +
                                    "  <group name=\"group1\">" + NEW_LINE +
                                    "    <metadata category=\"CategoryName\" value=\"val11\"/>" + NEW_LINE +
                                    "    <metadata category=\"CategoryName\" value=\"val12\"/>" + NEW_LINE +
                                    "    <metadata category=\"CategoryName\" value=\"val13\"/>" + NEW_LINE +
                                    "    <file name=\"" + rf1.getFile().getName() + "\">" + NEW_LINE +
                                    "      <path>" + rf1.getFile().getAbsolutePath() + "</path>" + NEW_LINE +
                                    "    </file>" + NEW_LINE +
                                    "  </group>" + NEW_LINE +
                                    "  <file name=\"" + rf2.getFile().getName() + "\">" + NEW_LINE +
                                    "    <path>" + rf2.getFile().getAbsolutePath() + "</path>" + NEW_LINE +
                                    "  </file>" + NEW_LINE +
                                    "</project>" + NEW_LINE;

        assertEquals(expectedXML, xml);
    }
}
