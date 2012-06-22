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
        final Project project = new Project(projectName, projectOwner, projectDescription);

        final MetadataCategory mdc1 = new MetadataCategory("CategoryName");
        final MetadataValue mdv11 = new MetadataValue("val11");
        final MetadataValue mdv12 = new MetadataValue("val12");
        final MetadataValue mdv13 = new MetadataValue("val13");
        final MetadataAssociation mda1 = new MetadataAssociation(mdc1);
        mda1.getMetadataValues().addAll(Arrays.asList(mdv11, mdv12, mdv13));

        final File file = new File("someResearchFile.txt");
        final ResearchFile rf = new ResearchFile(file);

        final Group group1 = new Group("group1");
        group1.getMetadataAssociations().add(mda1);
        group1.getResearchFiles().add(rf);

        project.getRootNode().getGroups().add(group1);

        final String xml = ProjectXMLBuilder.buildXML(project);

        final String expectedXML =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE +
                                    "<project description=\"some boring description\" name=\"projectName\" owner=\"project owner\">" + NEW_LINE +
                                    "  <group name=\"New Files\"/>" + NEW_LINE +
                                    "  <group name=\"group1\">" + NEW_LINE +
                                    "    <metadata category=\"CategoryName\">" + NEW_LINE +
                                    "      <value>val11</value>" + NEW_LINE +
                                    "      <value>val12</value>" + NEW_LINE +
                                    "      <value>val13</value>" + NEW_LINE +
                                    "    </metadata>" + NEW_LINE +
                                    "    <file>" + file.getAbsolutePath() + "</file>" + NEW_LINE +
                                    "  </group>" + NEW_LINE +
                                    "</project>" + NEW_LINE;

        assertEquals(expectedXML, xml);
    }
}
