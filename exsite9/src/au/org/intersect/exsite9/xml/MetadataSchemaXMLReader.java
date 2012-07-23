package au.org.intersect.exsite9.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Schema;

import static au.org.intersect.exsite9.xml.MetadataSchemaXMLUtils.*;

public final class MetadataSchemaXMLReader
{
    /**
     * Reads a Metadata Schema XML Document. Does not interact with the database to add the schema or its elements.
     * @param xmlDocument The XML to read. It must conform to the Schema.
     * @return The parsed Schema.
     * @throws ParserConfigurationException If there is a problem parsing the XML.
     * @throws SAXException If there is a parsing the XML.
     * @throws IOException If there is a problem parsing the XML.
     */
    public static Schema readXML(final Document xmlDocument)
    {
        final Element rootElement = xmlDocument.getDocumentElement();

        final Schema schema = parseRootElement(rootElement);

        final NodeList metadataCategories = rootElement.getElementsByTagName(ELEMENT_METADATACATEGORY);
        for (int i = 0; i < metadataCategories.getLength(); i++)
        {
            final Element metadataCategory = (Element) metadataCategories.item(i);
            addMetadataCategory(schema, metadataCategory);
        }

        return schema;
    }

    private static Schema parseRootElement(final Element rootElement)
    {
        final String name = rootElement.getAttribute(ATTRIBUTE_NAME).trim();
        final String description = rootElement.getAttribute(ATTRIBUTE_DESCRIPTION).trim();
        final String namespaceURL = rootElement.getAttribute(ATTRIBUTE_NAMESPACEURL).trim();
        return new Schema(name, description, namespaceURL, Boolean.FALSE);
    }

    private static void addMetadataCategory(final Schema schema, final Element metadataCategoryElement)
    {
        final String name = metadataCategoryElement.getAttribute(ATTRIBUTE_NAME).trim();
        final MetadataCategoryUse use = MetadataCategoryUse.valueOf(metadataCategoryElement.getAttribute(ATTRIBUTE_USE).trim());

        final MetadataCategory metadataCategory = new MetadataCategory(name, use);

        final NodeList metadataCategoryValues = metadataCategoryElement.getElementsByTagName(ELEMENT_VALUE);
        for (int i = 0; i < metadataCategoryValues.getLength(); i++)
        {
            final Element metadataCategoryValue = (Element) metadataCategoryValues.item(i);
            addMetadataCategoryValue(metadataCategory, metadataCategoryValue);
        }
        schema.getMetadataCategories().add(metadataCategory);
    }

    private static void addMetadataCategoryValue(final MetadataCategory metadataCategory, final Element metadataCategoryValueElement)
    {
        final String value = metadataCategoryValueElement.getTextContent().trim();
        metadataCategory.getValues().add(new MetadataValue(value));
    }
}
