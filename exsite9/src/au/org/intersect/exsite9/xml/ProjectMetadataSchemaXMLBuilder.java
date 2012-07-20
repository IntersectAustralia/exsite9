package au.org.intersect.exsite9.xml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.Schema;

public class ProjectMetadataSchemaXMLBuilder extends BaseXMLBuilder
{

    private static final Logger LOG = Logger.getLogger(ProjectMetadataSchemaXMLBuilder.class);
    
    public static String buildXML(Project project)
    {
        try
        {
            if (project.getSchema() == null)
            {
                return null;
            }
            
            final Document doc = createNewDocument();
            
            appendSchema(doc, project);
            
            appendMetadataCategories(doc, project);
            
            return transformDocumentToString(doc);
        }
        catch(ParserConfigurationException pce)
        {
            LOG.error(pce);
        }
        catch (final TransformerConfigurationException tce)
        {
            LOG.error(tce);
        }
        catch (final TransformerException te)
        {
            LOG.error(te);
        }
        return null;
    }
    
    private static void appendSchema(Document doc, Project project)
    {
        final Schema schema = project.getSchema();
        final Element schemaElement = doc.createElement("schema");
        schemaElement.setAttribute("description", schema.getDescription());
        schemaElement.setAttribute("name", schema.getName());
        schemaElement.setAttribute("namespace_url", schema.getNamespaceURL());
        doc.appendChild(schemaElement);
    }
    
    private static void appendMetadataCategories(Document doc, Project project)
    {
        for(final MetadataCategory category : project.getSchema().getMetadataCategories())
        {
            final Element catElement = doc.createElement("metadata_category");
            catElement.setAttribute("name",category.getName());
            catElement.setAttribute("use",category.getUse().toString());
            
            for(final MetadataValue value : category.getValues())
            {
                final Element valElement = doc.createElement("value");
                valElement.setTextContent(value.getValue());
                catElement.appendChild(valElement);
            }
            
            doc.getFirstChild().appendChild(catElement);
        }
    }
}
