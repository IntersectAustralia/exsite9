/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.xml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import au.org.intersect.exsite9.domain.MetadataAttribute;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataCategoryType;
import au.org.intersect.exsite9.domain.MetadataCategoryUse;
import au.org.intersect.exsite9.domain.Schema;
import au.org.intersect.exsite9.exception.InvalidSchemaException;

/**
 * Tests {@link MetadataSchemaXMLReader}
 */
public final class MetadataSchemaXMLReaderUnitTest
{
    private static final String NEW_LINE = System.getProperty("line.separator");

    @Test
    public void testReadXML() throws ParserConfigurationException, SAXException, IOException, InvalidSchemaException
    {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE 
            + "<schema description=\"\" name=\"project-schema\" namespace_url=\"\">" + NEW_LINE
            + "  <metadata_category name=\"cat1\" type=\"Controlled Vocabulary\" use=\"optional\">" + NEW_LINE
            + "    <value>value one</value>" + NEW_LINE
            + "  </metadata_category>" + NEW_LINE
            + "  <metadata_category inextensible=\"true\" name=\"cat2\" type=\"Free Text\" use=\"recommended\">" + NEW_LINE
            + "    <attribute name=\"attr1\">" + NEW_LINE
            + "      <value>mdav1</value>" + NEW_LINE
            + "      <value>mdav2</value>" + NEW_LINE
            + "    </attribute>" + NEW_LINE
            + "  </metadata_category>" + NEW_LINE
            + "</schema>" + NEW_LINE;

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));

        final Schema schema = MetadataSchemaXMLReader.readXML(document);
        assertNotNull(schema);

        assertEquals("project-schema", schema.getName());
        assertEquals("", schema.getNamespaceURL());
        assertFalse(schema.getLocal());

        assertEquals(2, schema.getMetadataCategories().size());
        final MetadataCategory cat1 = schema.getMetadataCategories().get(0);
        assertEquals("cat1", cat1.getName());
        assertEquals(MetadataCategoryType.CONTROLLED_VOCABULARY, cat1.getType());
        assertEquals(MetadataCategoryUse.optional, cat1.getUse());
        assertEquals(1, cat1.getValues().size());
        assertEquals("value one", cat1.getValues().get(0).getValue());
        assertNull(cat1.getMetadataAttribute());
        final MetadataCategory cat2 = schema.getMetadataCategories().get(1);
        assertEquals("cat2", cat2.getName());
        assertEquals(MetadataCategoryType.FREETEXT, cat2.getType());
        assertEquals(MetadataCategoryUse.recommended, cat2.getUse());
        assertEquals(0, cat2.getValues().size());
        final MetadataAttribute attr1 = cat2.getMetadataAttribute();
        assertNotNull(attr1);
        assertEquals("attr1", attr1.getName());
        final List<MetadataAttributeValue> attrValues = attr1.getMetadataAttributeValues();
        assertEquals(2, attrValues.size());
        assertEquals("mdav1", attrValues.get(0).getValue());
        assertEquals("mdav2", attrValues.get(1).getValue());
    }

    @Test
    public void testReadXMLBadCategoryName() throws ParserConfigurationException, SAXException, IOException, InvalidSchemaException
    {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE 
            + "<schema description=\"\" name=\"project-schema\" namespace_url=\"\">" + NEW_LINE
            + "  <metadata_category name=\"cat1 bad\" type=\"Controlled Vocabulary\" use=\"optional\">" + NEW_LINE
            + "    <value>value one</value>" + NEW_LINE
            + "  </metadata_category>" + NEW_LINE
            + "  <metadata_category name=\"cat2\" type=\"Free Text\" use=\"recommended\"/>" + NEW_LINE
            + "</schema>" + NEW_LINE;

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));

        try
        {
            MetadataSchemaXMLReader.readXML(document);
            fail();
        }
        catch (final InvalidSchemaException e)
        {
        }
    }

    @Test
    public void testReadXMLBadValue() throws ParserConfigurationException, SAXException, IOException, InvalidSchemaException
    {
        final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" + NEW_LINE 
            + "<schema description=\"\" name=\"project-schema\" namespace_url=\"\">" + NEW_LINE
            + "  <metadata_category name=\"cat1\" type=\"Controlled Vocabulary\" use=\"optional\">" + NEW_LINE
            + "    <value></value>" + NEW_LINE
            + "  </metadata_category>" + NEW_LINE
            + "  <metadata_category name=\"cat2\" type=\"Free Text\" use=\"recommended\"/>" + NEW_LINE
            + "</schema>" + NEW_LINE;

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));

        try
        {
            MetadataSchemaXMLReader.readXML(document);
            fail();
        }
        catch (final InvalidSchemaException e)
        {
        }
    }
}
