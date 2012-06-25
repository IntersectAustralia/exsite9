/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.xml;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Builds XML for a provided {@link Project}.
 */
public final class ProjectXMLBuilder
{
    private static final Logger LOG = Logger.getLogger(ProjectXMLBuilder.class);

    /**
     * Builds the XML for a provided {@link Project} tree.
     * @param project The project to build XML for.
     * @return The String with the XML, or {@code null} if XML generation was unsuccessful.
     */
    public static String buildXML(final Project project)
    {
        try
        {
            final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();

            final Document doc = documentBuilder.newDocument();

            final Element rootElement = doc.createElement("project");
            doc.appendChild(rootElement);

            rootElement.setAttribute("name", project.getName());
            rootElement.setAttribute("owner", project.getOwner());
            rootElement.setAttribute("description", project.getDescription());

            for (final Group group : project.getRootNode().getGroups())
            {
                appendGroup(doc, rootElement, group);
            }

            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            final DOMSource source = new DOMSource(doc);

            final StringWriter stringWriter = new StringWriter();
            final StreamResult streamResult = new StreamResult(stringWriter);

            transformer.transform(source, streamResult);

            return stringWriter.toString();

        }
        catch (final ParserConfigurationException e)
        {
            LOG.error(e);
        }
        catch (final TransformerConfigurationException e)
        {
            LOG.error(e);
        }
        catch (final TransformerException e)
        {
            LOG.error(e);
        }
        return null;
    }

    /**
     * @param parent
     * @param group
     */
    private static void appendGroup(final Document doc, final Element parent, final Group group)
    {
        final Element groupElement = doc.createElement("group");
        groupElement.setAttribute("name", group.getName());

        for (final MetadataAssociation metadataAssociation : group.getMetadataAssociations())
        {
            appendMetadataAssociation(doc, groupElement, metadataAssociation);
        }

        for (final Group childGroup : group.getGroups())
        {
            // Recursion :(
            appendGroup(doc, groupElement, childGroup);
        }

        for (final ResearchFile childFile : group.getResearchFiles())
        {
            appendResearchFile(doc, groupElement, childFile);
        }

        parent.appendChild(groupElement);
    }

    private static void appendMetadataAssociation(final Document doc, final Element parent, final MetadataAssociation metadataAssociation)
    {
        final Element metadataAssociationElement = doc.createElement("metadata");
        metadataAssociationElement.setAttribute("category", metadataAssociation.getMetadataCategory().getName());

        for (final MetadataValue metadataValue : metadataAssociation.getMetadataValues())
        {
            final Element metadataValueElement = doc.createElement("value");
            metadataValueElement.appendChild(doc.createTextNode(metadataValue.getValue()));
            metadataAssociationElement.appendChild(metadataValueElement);
        }

        parent.appendChild(metadataAssociationElement);
    }

    private static void appendResearchFile(final Document doc, final Element parent, final ResearchFile researchFile)
    {
        final Element researchFileElement = doc.createElement("file");
        researchFileElement.appendChild(doc.createTextNode(researchFile.getFile().getName()));
        parent.appendChild(researchFileElement);
    }
}
