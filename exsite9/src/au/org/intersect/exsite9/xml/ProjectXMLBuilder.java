/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.xml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Builds XML for a provided {@link Project}.
 */
public final class ProjectXMLBuilder extends BaseXMLBuilder
{
    private static final Logger LOG = Logger.getLogger(ProjectXMLBuilder.class);

    /**
     * Builds the XML for a provided {@link Project} tree.
     * 
     * @param project
     *            The project to build XML for.
     * @return The String with the XML, or {@code null} if XML generation was unsuccessful.
     */
    public static String buildXML(final Project project)
    {
        try
        {
            final Document doc = createNewDocument();
            
            final Element rootElement = createProjectRootElement(doc, project);
            doc.appendChild(rootElement);
            
            for (final Group group : project.getRootNode().getGroups())
            {
                appendGroup(doc, rootElement, group);
            }

            for (final ResearchFile researchFile : project.getRootNode().getResearchFiles())
            {
                appendResearchFile(doc, rootElement, researchFile);
            }

            return transformDocumentToString(doc);
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
}
