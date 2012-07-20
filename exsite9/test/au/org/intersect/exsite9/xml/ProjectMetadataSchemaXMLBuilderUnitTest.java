package au.org.intersect.exsite9.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.Schema;

public class ProjectMetadataSchemaXMLBuilderUnitTest
{
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String EMPTY_STRING = "";
    
    @Test
    public void testBuildXML()
    {
        Project project = new Project();
        project.setName("Project");
        
        Schema schema = new Schema();
        project.setSchema(schema);
        schema.setName("project-schema");
        
        MetadataCategory cat1 = new MetadataCategory("cat1");
        schema.getMetadataCategories().add(cat1);
        
        MetadataValue cat1Val1 = new MetadataValue("value one");
        cat1.getValues().add(cat1Val1);
        
        String expectedXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE 
                           + "<schema description=\"" + EMPTY_STRING + "\" name=\"project-schema\" namespace_url=\"" + EMPTY_STRING + "\">" + NEW_LINE
                           + "  <metadata_category name=\"cat1\" use=\"optional\">" + NEW_LINE
                           + "    <value>value one</value>" + NEW_LINE
                           + "  </metadata_category>" + NEW_LINE
                           + "</schema>" + NEW_LINE;
        
        String actualXML = ProjectMetadataSchemaXMLBuilder.buildXML(project);
        
        assertEquals("XML matches", expectedXML, actualXML);
    }
}
