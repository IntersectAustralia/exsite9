package au.org.intersect.exsite9.xml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.Schema;

import static au.org.intersect.exsite9.xml.MetadataSchemaXMLUtils.*;

public final class MetadataSchemaXMLBuilder extends BaseXMLBuilder
{
    private static final Logger LOG = Logger.getLogger(MetadataSchemaXMLBuilder.class);
    
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
        final Element schemaElement = doc.createElement(ELEMENT_SCHEMA);
        schemaElement.setAttribute(ATTRIBUTE_DESCRIPTION, schema.getDescription());
        schemaElement.setAttribute(ATTRIBUTE_NAME, schema.getName());
        schemaElement.setAttribute(ATTRIBUTE_NAMESPACEURL, schema.getNamespaceURL());
        doc.appendChild(schemaElement);
    }
    
    private static void appendMetadataCategories(Document doc, Project project)
    {
        for(final MetadataCategory category : project.getSchema().getMetadataCategories())
        {
            final Element catElement = doc.createElement(ELEMENT_METADATACATEGORY);
            catElement.setAttribute(ATTRIBUTE_NAME, category.getName());
            catElement.setAttribute(ATTRIBUTE_USE, category.getUse().toString());

            final MetadataCategoryType type = category.getType();
            catElement.setAttribute(ATTRIBUTE_TYPE, type.toString());
            catElement.setAttribute(ATTRIBUTE_INEXTENSIBLE, ((Boolean)category.isInextensible()).toString());

            if (type != MetadataCategoryType.FREETEXT)
            {
                for(final MetadataValue value : category.getValues())
                {
                    final Element valElement = doc.createElement(ELEMENT_VALUE);
                    valElement.setTextContent(value.getValue());
                    catElement.appendChild(valElement);
                }
            }
            doc.getFirstChild().appendChild(catElement);
        }
    }
}
