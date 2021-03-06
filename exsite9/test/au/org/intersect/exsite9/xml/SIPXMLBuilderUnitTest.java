/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.xml;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.domain.FieldOfResearch;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.domain.SubmissionPackage;
import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Tests {@link SIPXMLBuilder}
 */
public final class SIPXMLBuilderUnitTest
{
    private static final String NEW_LINE = System.getProperty("line.separator");

    @Test
    public void testBuildSIPXML()
    {
        final String projectName = "projectName";
        final String projectOwner = "project owner";
        final String institution = "institsutions";
        final String email = "some@email.com";
        final String projectDescription = "some boring description";
        final String projectAccessRights = "admin";
        final String projectCollectionType = "Dataset";
        final String projectDatesOfCapture = "next week";
        final String geoCoverage = "up and left a little";
        final String projectLicence = "to kill";
        final String projectPhysicalLocation = "Sydney";
        final String projectRelatedParty = "Intersect";
        final String projectSubject = "random";
        final String rightsStatement = "miranda rights";
        final String identifier = "18+";
        final String electronicLocation = "github";
        final String placeOrRegionName = "Mexico";
        final String citationInformation = "some citation";
        final String countries = "libya";
        final String languages = "english";
        final FieldOfResearch fieldOfResearch = new FieldOfResearch("code", "name");
        final String fundingBody = "fundingBody";
        final String grantID = "grantID";
        final String relatedGrant = "some related grant that is nice.";
        final String relatedInformation = "related information";

        final Project project = new Project(new ProjectFieldsDTO(projectName, projectOwner, institution, email, projectDescription,
                projectCollectionType, rightsStatement, projectAccessRights, projectLicence, identifier, projectSubject,
                electronicLocation, projectPhysicalLocation, placeOrRegionName, geoCoverage, projectDatesOfCapture,
                citationInformation, countries, languages, fieldOfResearch, fundingBody, grantID, projectRelatedParty, relatedGrant, relatedInformation));
        final MetadataCategory mdc1 = new MetadataCategory("CategoryName", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue mdv11 = new MetadataValue("val11");
        final MetadataValue mdv12 = new MetadataValue("val12");
        final MetadataValue mdv13 = new MetadataValue("val13");
        final MetadataAssociation mda1 = new MetadataAssociation(mdc1);
        mda1.getMetadataValues().addAll(Arrays.asList(mdv11, mdv12, mdv13));

        final File file1 = Mockito.mock(File.class);
        final File file2 = Mockito.mock(File.class);
        when(file1.exists()).thenReturn(true);
        when(file2.exists()).thenReturn(true);
        when(file1.getName()).thenReturn("someResearchFile.txt");
        when(file2.getName()).thenReturn("someOtherResearchFile.txt");
        when(file1.getAbsolutePath()).thenReturn("/Some/Path/someResearchFile.txt");
        when(file2.getAbsolutePath()).thenReturn("/Some/Other/Path/someOtherResearchFile.txt");

        final ResearchFile rf1 = new ResearchFile(file1);
        final ResearchFile rf2 = new ResearchFile(file2);

        final Group group1 = new Group("group1");
        group1.getMetadataAssociations().add(mda1);
        group1.getResearchFiles().add(rf1);
        rf1.setParentGroup(group1);

        final Group innerGroup1 = new Group("innerGroup1");
        group1.getGroups().add(innerGroup1);

        project.getRootNode().getGroups().add(group1);
        project.getRootNode().getResearchFiles().add(rf2);
        rf2.setParentGroup(project.getRootNode());

        group1.setParentGroup(project.getRootNode());
        innerGroup1.setParentGroup(group1);

        final String submissionPackageName = "submission package name";
        final String submissionPackageDescription = "submission package description";
        final List<ResearchFile> selectedFilesForSubmission = Arrays.asList(rf1, rf2);
        final SubmissionPackage submissionPackage = new SubmissionPackage(submissionPackageName, submissionPackageDescription, selectedFilesForSubmission);
        final String xmlOut = SIPXMLBuilder.buildXML(project, GroupDAO.getGroupsContainingSelectedFiles(selectedFilesForSubmission), submissionPackage, false);

        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE +
                                "<project>" + NEW_LINE +
                                "  <project_info collectionType=\"" + projectCollectionType + "\" identifier=\"" + identifier + "\">" + NEW_LINE +
                                "    <projectName>" + projectName + "</projectName>" + NEW_LINE +
                                "    <name>" + projectOwner + "</name>" + NEW_LINE +
                                "    <institution>" + institution + "</institution>" + NEW_LINE +
                                "    <email>" + email + "</email>" + NEW_LINE +
                                "    <description>" + projectDescription +"</description>" + NEW_LINE +
                                "    <rightsStatement>" + rightsStatement + "</rightsStatement>" + NEW_LINE +
                                "    <accessRights>" + projectAccessRights + "</accessRights>" + NEW_LINE +
                                "    <license>" + projectLicence + "</license>" + NEW_LINE +
                                "    <subject>" + projectSubject + "</subject>" + NEW_LINE +
                                "    <electronicLocation>" + electronicLocation + "</electronicLocation>" + NEW_LINE +
                                "    <physicalLocation>" + projectPhysicalLocation + "</physicalLocation>" + NEW_LINE +
                                "    <placeOrRegionName>" + placeOrRegionName + "</placeOrRegionName>" + NEW_LINE +
                                "    <geographicalCoverage>" + geoCoverage + "</geographicalCoverage>" + NEW_LINE +
                                "    <datesOfCapture>" + projectDatesOfCapture + "</datesOfCapture>" + NEW_LINE +
                                "    <citationInformation>" + citationInformation + "</citationInformation>" + NEW_LINE +
                                "    <countries>" + countries + "</countries>" + NEW_LINE +
                                "    <languages>" + languages + "</languages>" + NEW_LINE +
                                "    <fieldOfResearch>" + fieldOfResearch.toString() + "</fieldOfResearch>" + NEW_LINE +
                                "    <fundingBody>" + fundingBody + "</fundingBody>" + NEW_LINE +
                                "    <grantID>" + grantID + "</grantID>" + NEW_LINE +
                                "    <relatedParty>" + projectRelatedParty + "</relatedParty>" + NEW_LINE +
                                "    <relatedGrant>" + relatedGrant + "</relatedGrant>" + NEW_LINE +
                                "    <relatedInformation>" + relatedInformation + "</relatedInformation>" + NEW_LINE +
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

        assertEquals(expected, xmlOut);
    }

    @Test
    public void testBuildSIPXMLWithGroupPaths()
    {
        final String projectName = "projectName";
        final String projectOwner = "project owner";
        final String institution = "institsutions";
        final String email = "some@email.com";
        final String projectDescription = "some boring description";
        final String projectAccessRights = "admin";
        final String projectCollectionType = "Dataset";
        final String projectDatesOfCapture = "next week";
        final String geoCoverage = "up and left a little";
        final String projectLicence = "to kill";
        final String projectPhysicalLocation = "Sydney";
        final String projectRelatedParty = "Intersect";
        final String projectSubject = "random";
        final String rightsStatement = "miranda rights";
        final String identifier = "18+";
        final String electronicLocation = "github";
        final String placeOrRegionName = "Mexico";
        final String citationInformation = "some citation";
        final String countries = "libya";
        final String languages = "english";
        final FieldOfResearch fieldOfResearch = new FieldOfResearch("code", "name");
        final String fundingBody = "fundingBody";
        final String grantID = "grantID";
        final String relatedGrant = "some related grant that is nice.";
        final String relatedInformation = "related information";

        final Project project = new Project(new ProjectFieldsDTO(projectName, projectOwner, institution, email, projectDescription,
                projectCollectionType, rightsStatement, projectAccessRights, projectLicence, identifier, projectSubject,
                electronicLocation, projectPhysicalLocation, placeOrRegionName, geoCoverage, projectDatesOfCapture,
                citationInformation, countries, languages, fieldOfResearch, fundingBody, grantID, projectRelatedParty, relatedGrant, relatedInformation));
        final MetadataCategory mdc1 = new MetadataCategory("CategoryName", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        final MetadataValue mdv11 = new MetadataValue("val11");
        final MetadataValue mdv12 = new MetadataValue("val12");
        final MetadataValue mdv13 = new MetadataValue("val13");
        final MetadataAssociation mda1 = new MetadataAssociation(mdc1);
        mda1.getMetadataValues().addAll(Arrays.asList(mdv11, mdv12, mdv13));

        final File file1 = Mockito.mock(File.class);
        final File file2 = Mockito.mock(File.class);
        when(file1.exists()).thenReturn(true);
        when(file2.exists()).thenReturn(true);
        when(file1.getName()).thenReturn("someResearchFile.txt");
        when(file2.getName()).thenReturn("someOtherResearchFile.txt");
        when(file1.getAbsolutePath()).thenReturn("/Some/Path/someResearchFile.txt");
        when(file2.getAbsolutePath()).thenReturn("/Some/Other/Path/someOtherResearchFile.txt");

        final ResearchFile rf1 = new ResearchFile(file1);
        final ResearchFile rf2 = new ResearchFile(file2);

        final Group group1 = new Group("group1");
        group1.getMetadataAssociations().add(mda1);
        group1.getResearchFiles().add(rf1);
        rf1.setParentGroup(group1);

        final Group innerGroup1 = new Group("innerGroup1");
        group1.getGroups().add(innerGroup1);

        project.getRootNode().getGroups().add(group1);
        project.getRootNode().getResearchFiles().add(rf2);
        rf2.setParentGroup(project.getRootNode());

        group1.setParentGroup(project.getRootNode());
        innerGroup1.setParentGroup(group1);

        final String submissionPackageName = "submission package name";
        final String submissionPackageDescription = "submission package description";
        final List<ResearchFile> selectedFilesForSubmission = Arrays.asList(rf1, rf2);
        final SubmissionPackage submissionPackage = new SubmissionPackage(submissionPackageName, submissionPackageDescription, selectedFilesForSubmission);
        final String xmlOut = SIPXMLBuilder.buildXML(project, GroupDAO.getGroupsContainingSelectedFiles(selectedFilesForSubmission), submissionPackage, true);

        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE +
                                "<project>" + NEW_LINE +
                                "  <project_info collectionType=\"" + projectCollectionType + "\" identifier=\"" + identifier + "\">" + NEW_LINE +
                                "    <projectName>" + projectName + "</projectName>" + NEW_LINE +
                                "    <name>" + projectOwner + "</name>" + NEW_LINE +
                                "    <institution>" + institution + "</institution>" + NEW_LINE +
                                "    <email>" + email + "</email>" + NEW_LINE +
                                "    <description>" + projectDescription +"</description>" + NEW_LINE +
                                "    <rightsStatement>" + rightsStatement + "</rightsStatement>" + NEW_LINE +
                                "    <accessRights>" + projectAccessRights + "</accessRights>" + NEW_LINE +
                                "    <license>" + projectLicence + "</license>" + NEW_LINE +
                                "    <subject>" + projectSubject + "</subject>" + NEW_LINE +
                                "    <electronicLocation>" + electronicLocation + "</electronicLocation>" + NEW_LINE +
                                "    <physicalLocation>" + projectPhysicalLocation + "</physicalLocation>" + NEW_LINE +
                                "    <placeOrRegionName>" + placeOrRegionName + "</placeOrRegionName>" + NEW_LINE +
                                "    <geographicalCoverage>" + geoCoverage + "</geographicalCoverage>" + NEW_LINE +
                                "    <datesOfCapture>" + projectDatesOfCapture + "</datesOfCapture>" + NEW_LINE +
                                "    <citationInformation>" + citationInformation + "</citationInformation>" + NEW_LINE +
                                "    <countries>" + countries + "</countries>" + NEW_LINE +
                                "    <languages>" + languages + "</languages>" + NEW_LINE +
                                "    <fieldOfResearch>" + fieldOfResearch.toString() + "</fieldOfResearch>" + NEW_LINE +
                                "    <fundingBody>" + fundingBody + "</fundingBody>" + NEW_LINE +
                                "    <grantID>" + grantID + "</grantID>" + NEW_LINE +
                                "    <relatedParty>" + projectRelatedParty + "</relatedParty>" + NEW_LINE +
                                "    <relatedGrant>" + relatedGrant + "</relatedGrant>" + NEW_LINE +
                                "    <relatedInformation>" + relatedInformation + "</relatedInformation>" + NEW_LINE +
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
                                "          <path>group1/" + rf1.getFile().getName() + "</path>" + NEW_LINE +
                                "        </file>" + NEW_LINE +
                                "      </files>" + NEW_LINE +
                                "    </group>" + NEW_LINE +
                                "  </groups>" + NEW_LINE +
                                "  <files numFiles=\"1\">" + NEW_LINE +
                                "    <file>" + NEW_LINE +
                                "      <name>" + rf2.getFile().getName() + "</name>" + NEW_LINE +
                                "      <path>" + rf2.getFile().getName() + "</path>" + NEW_LINE +
                                "    </file>" + NEW_LINE +
                                "  </files>" + NEW_LINE +
                                "</project>" + NEW_LINE;

        assertEquals(expected, xmlOut);
    }
}
