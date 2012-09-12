/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.xml;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.Schema;

/**
 * Tests {@link MetadataSchemaXMLBuilder}
 */
public class MetadataSchemaXMLBuilderUnitTest
{
    private static final String NEW_LINE = System.getProperty("line.separator");
    
    @Test
    public void testBuildXML()
    {
        final Project project = new Project();
        project.setName("Project");
        assertNull(MetadataSchemaXMLBuilder.buildXML(project));
        
        final Schema schema = new Schema();
        project.setSchema(schema);
        schema.setName("project-schema");
        
        final MetadataCategory cat1 = new MetadataCategory("cat1", MetadataCategoryType.CONTROLLED_VOCABULARY, MetadataCategoryUse.optional);
        cat1.setDescription("This is cat1.");
        schema.getMetadataCategories().add(cat1);

        final MetadataCategory cat2 = new MetadataCategory("cat2", MetadataCategoryType.FREETEXT, MetadataCategoryUse.recommended);
        cat2.setInextensible(true);

        final MetadataAttributeValue attributeValue1 = new MetadataAttributeValue("mdav1");
        final MetadataAttributeValue attributeValue2 = new MetadataAttributeValue("mdav2");
        final MetadataAttribute attribute1 = new MetadataAttribute("attr1", Arrays.asList(attributeValue1, attributeValue2));
        cat2.setMetadataAttribute(attribute1);
        schema.getMetadataCategories().add(cat2);
        
        final MetadataValue cat1Val1 = new MetadataValue("value one");
        cat1.getValues().add(cat1Val1);

        final MetadataValue cat2Val1 = new MetadataValue("value two");
        cat2.getValues().add(cat2Val1);
        
        final String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE 
                           + "<schema description=\"\" name=\"project-schema\" namespace_url=\"\">" + NEW_LINE
                           + "  <metadata_category inextensible=\"false\" name=\"cat1\" type=\"Controlled Vocabulary\" use=\"optional\">" + NEW_LINE
                           + "    <description>This is cat1.</description>" + NEW_LINE
                           + "    <value>value one</value>" + NEW_LINE
                           + "  </metadata_category>" + NEW_LINE
                           + "  <metadata_category inextensible=\"true\" name=\"cat2\" type=\"Free Text\" use=\"recommended\">" + NEW_LINE
                           + "    <attribute name=\"attr1\">" + NEW_LINE
                           + "      <value>mdav1</value>" + NEW_LINE
                           + "      <value>mdav2</value>" + NEW_LINE
                           + "    </attribute>" + NEW_LINE
                           + "  </metadata_category>" + NEW_LINE
                           + "</schema>" + NEW_LINE;
        
        final String actualXML = MetadataSchemaXMLBuilder.buildXML(project);
        assertEquals("XML matches", expectedXML, actualXML);
    }
}
