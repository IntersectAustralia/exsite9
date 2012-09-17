package au.org.intersect.exsite9.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.exception.InvalidSchemaException;
import au.org.intersect.exsite9.validators.MetadataAttributeNameValidator;
import au.org.intersect.exsite9.validators.MetadataAttributeValueValidator;
import au.org.intersect.exsite9.validators.MetadataCategoryNameValidator;
import au.org.intersect.exsite9.validators.MetadataValueValidator;

import static au.org.intersect.exsite9.xml.MetadataSchemaXMLUtils.*;

public final class MetadataSchemaXMLReader
{
    /**
     * Reads a Metadata Schema XML Document. Does not interact with the database to add the schema or its elements.
     * @param xmlDocument The XML to read. It must conform to the Schema.
     * @return The parsed Schema.
     * @throws InvalidSchemaException If there is something wrong with parsing the Schema. 
     */
    public static Schema readXML(final Document xmlDocument) throws InvalidSchemaException
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

    private static Schema parseRootElement(final Element rootElement) throws InvalidSchemaException
    {
        final String name = rootElement.getAttribute(ATTRIBUTE_NAME).trim();
        if (name.length() >= 255)
        {
            throw new InvalidSchemaException("Schema name is too long.");
        }

        final String description = rootElement.getAttribute(ATTRIBUTE_DESCRIPTION).trim();
        final String namespaceURL = rootElement.getAttribute(ATTRIBUTE_NAMESPACEURL).trim();
        return new Schema(name, description, namespaceURL, Boolean.FALSE);
    }

    private static void addMetadataCategory(final Schema schema, final Element metadataCategoryElement) throws InvalidSchemaException
    {
        final String name = metadataCategoryElement.getAttribute(ATTRIBUTE_NAME).trim();

        // Ensure the category name is valid
        final MetadataCategoryNameValidator validator = new MetadataCategoryNameValidator(schema.getMetadataCategories(), null);
        if (!validator.isValid(name))
        {
            throw new InvalidSchemaException("Metadata category '" + name + "' is invalid. " + validator.getErrorMessage());
        }

        final MetadataCategoryUse use = MetadataCategoryUse.valueOf(metadataCategoryElement.getAttribute(ATTRIBUTE_USE).trim());
        final MetadataCategoryType type = MetadataCategoryType.fromString(metadataCategoryElement.getAttribute(ATTRIBUTE_TYPE).trim());
        
        String description = "";
        final NodeList descriptionNodes = metadataCategoryElement.getElementsByTagName(ELEMENT_DESCRIPTION);
        if (descriptionNodes.item(0) != null){
            description = descriptionNodes.item(0).getTextContent().trim();
        }
        
        final boolean inextensible = Boolean.valueOf(metadataCategoryElement.getAttribute(ATTRIBUTE_INEXTENSIBLE).trim());

        final MetadataCategory metadataCategory = new MetadataCategory(name, type, use);
        metadataCategory.setDescription(description);
        metadataCategory.setInextensible(inextensible);
        metadataCategory.setImported(true);

        if (type != MetadataCategoryType.FREETEXT)
        {
            final NodeList metadataCategoryValues = metadataCategoryElement.getElementsByTagName(ELEMENT_VALUE);
            for (int i = 0; i < metadataCategoryValues.getLength(); i++)
            {
                final Element metadataCategoryValue = (Element) metadataCategoryValues.item(i);
                addMetadataCategoryValue(metadataCategory, metadataCategoryValue);
            }
        }
        else
        {
            final NodeList attributeElementList = metadataCategoryElement.getElementsByTagName(ELEMENT_ATTRIBUTE);
            if (attributeElementList.getLength() > 1)
            {
                throw new InvalidSchemaException("Only one " + ELEMENT_ATTRIBUTE + " element permitted as a child of a free-text metadata category");
            }
            if (attributeElementList.getLength() > 0)
            {
                final Element attribtueElement = (Element) attributeElementList.item(0);
                final String attributeName = attribtueElement.getAttribute(ATTRIBUTE_NAME);
                final MetadataAttributeNameValidator manValidator = new MetadataAttributeNameValidator(false);
                if (!manValidator.isValid(attributeName))
                {
                    throw new InvalidSchemaException("Metadata attribute name for '" + name + "' is invalid. " + validator.getErrorMessage());
                }

                final MetadataAttribute metadataAttribute = new MetadataAttribute();
                metadataAttribute.setName(attributeName);

                final NodeList attributeNodeValuesList = attribtueElement.getElementsByTagName(ELEMENT_VALUE);
                for (int i = 0; i < attributeNodeValuesList.getLength(); i++)
                {
                    final Element metadataAttributeValue = (Element) attributeNodeValuesList.item(i);
                    addMetadataAttributeValue(metadataAttribute, metadataAttributeValue);
                }

                metadataCategory.setMetadataAttribute(metadataAttribute);
            }
        }

        schema.getMetadataCategories().add(metadataCategory);
    }

    private static void addMetadataCategoryValue(final MetadataCategory metadataCategory, final Element metadataCategoryValueElement) throws InvalidSchemaException
    {
        final String value = metadataCategoryValueElement.getTextContent().trim();
        final MetadataValueValidator validator = new MetadataValueValidator(metadataCategory.getValues());
        if (!validator.isValid(value))
        {
            throw new InvalidSchemaException("Metadata value '" + value + "' in category '" + metadataCategory.getName() + "' is invalid. " + validator.getErrorMessage());
        }
        metadataCategory.getValues().add(new MetadataValue(value));
    }

    private static void addMetadataAttributeValue(final MetadataAttribute metadataAttribute, final Element metadataAttributeValueElement) throws InvalidSchemaException
    {
        final String value = metadataAttributeValueElement.getTextContent().trim();
        final MetadataAttributeValueValidator validator = new MetadataAttributeValueValidator(metadataAttribute.getMetadataAttributeValues());
        if (!validator.isValid(value))
        {
            throw new InvalidSchemaException("Metadata attribute value '" + value + "' in attribute '" + metadataAttribute.getName() + "' is invalid. " + validator.getErrorMessage());
        }
        metadataAttribute.getMetadataAttributeValues().add(new MetadataAttributeValue(value));
    }
}
