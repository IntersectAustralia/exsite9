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
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
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
        final String rightsStatement = "miranda rights";
        final String identifier = "18+";
        final String electronicLocation = "github";
        final String placeOrRegionName = "Mexico";
        final String citationInformation = "some citation";
        final String relatedActivity = "some related activity that is nice.";
        final String relatedInformation = "related information";

        final Project project = new Project(new ProjectFieldsDTO(projectName, projectOwner, projectDescription,
                projectCollectionType, rightsStatement, projectAccessRights, projectLicence, identifier, projectSubject,
                electronicLocation, projectPhysicalLocation, placeOrRegionName, projectLatLong, projectDatesOfCapture,
                citationInformation, projectRelatedParty, relatedActivity, relatedInformation));
        final MetadataCategory mdc1 = new MetadataCategory("CategoryName", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
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

        final Group innerGroup1 = new Group("innerGroup1");
        group1.getGroups().add(innerGroup1);

        project.getRootNode().getGroups().add(group1);
        project.getRootNode().getResearchFiles().add(rf2);

        final String xml = ProjectXMLBuilder.buildXML(project);

        final String expectedXML =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE +
                                    "<project>" + NEW_LINE +
                                    "  <project_info collectionType=\"" + projectCollectionType + "\" identifier=\"" + identifier + "\">" + NEW_LINE +
                                    "    <name>" + projectName + "</name>" + NEW_LINE +
                                    "    <description>" + projectDescription +"</description>" + NEW_LINE +
                                    "    <owner>" + projectOwner + "</owner>" + NEW_LINE +
                                    "    <accessRights>" + projectAccessRights + "</accessRights>" + NEW_LINE +
                                    "    <citationInformation>" + citationInformation + "</citationInformation>" + NEW_LINE +
                                    "    <datesOfCapture>" + projectDatesOfCapture + "</datesOfCapture>" + NEW_LINE +
                                    "    <electronicLocation>" + electronicLocation + "</electronicLocation>" + NEW_LINE +
                                    "    <latitudeLongitude>" + projectLatLong + "</latitudeLongitude>" + NEW_LINE +
                                    "    <license>" + projectLicence + "</license>" + NEW_LINE +
                                    "    <physicalLocation>" + projectPhysicalLocation + "</physicalLocation>" + NEW_LINE +
                                    "    <placeOrRegionName>" + placeOrRegionName + "</placeOrRegionName>" + NEW_LINE +
                                    "    <relatedActivity>" + relatedActivity + "</relatedActivity>" + NEW_LINE +
                                    "    <relatedInformation>" + relatedInformation + "</relatedInformation>" + NEW_LINE +
                                    "    <relatedParty>" + projectRelatedParty + "</relatedParty>" + NEW_LINE +
                                    "    <rightsStatement>" + rightsStatement + "</rightsStatement>" + NEW_LINE +
                                    "    <subject>" + projectSubject + "</subject>" + NEW_LINE +
                                    "  </project_info>" + NEW_LINE +
                                    "  <groups numGroups=\"2\">" + NEW_LINE +
                                    "    <group name=\"New Files\"/>" + NEW_LINE +
                                    "    <group name=\"group1\">" + NEW_LINE +
                                    "      <CategoryName>val11</CategoryName>" + NEW_LINE +
                                    "      <CategoryName>val12</CategoryName>" + NEW_LINE +
                                    "      <CategoryName>val13</CategoryName>" + NEW_LINE +
                                    "      <groups numGroups=\"1\">" + NEW_LINE +
                                    "        <group name=\"innerGroup1\"/>" + NEW_LINE +
                                    "      </groups>" + NEW_LINE +
                                    "      <files numFiles=\"1\">" + NEW_LINE +
                                    "        <file>" + NEW_LINE +
                                    "          <name>" + rf1.getFile().getName() + "</name>" + NEW_LINE +
                                    "          <path>" + rf1.getFile().getAbsolutePath() + "</path>" + NEW_LINE +
                                    "        </file>" + NEW_LINE +
                                    "      </files>" + NEW_LINE +
                                    "    </group>" + NEW_LINE +
                                    "  </groups>" + NEW_LINE +
                                    "  <files numFiles=\"1\">" + NEW_LINE +
                                    "    <file>" + NEW_LINE +
                                    "      <name>" + rf2.getFile().getName() + "</name>" + NEW_LINE +
                                    "      <path>" + rf2.getFile().getAbsolutePath() + "</path>" + NEW_LINE +
                                    "    </file>" + NEW_LINE +
                                    "  </files>" + NEW_LINE +
                                    "</project>" + NEW_LINE;

        assertEquals(expectedXML, xml);
    }
}
