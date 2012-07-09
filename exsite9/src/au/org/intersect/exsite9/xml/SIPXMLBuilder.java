package au.org.intersect.exsite9.xml;

import java.util.List;

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

public class SIPXMLBuilder extends BaseXMLBuilder
{

    private static final Logger LOG = Logger.getLogger(SIPXMLBuilder.class);
    
    public static String buildXML(Project project, List<Group> selectedGroups, List<ResearchFile> selectedFiles)
    {
        try
        {
            final Document doc = createNewDocument();
            
            final Element rootElement = createProjectRootElement(doc, project);
            doc.appendChild(rootElement);

            for (final Group group : project.getRootNode().getGroups())
            {
                if(selectedGroups.contains(group))
                {
                    appendGroup(doc, selectedGroups, selectedFiles, rootElement, group);
                }
            }

            for (final ResearchFile researchFile : project.getRootNode().getResearchFiles())
            {
                if(selectedFiles.contains(researchFile))
                {
                    appendResearchFile(doc, rootElement, researchFile);
                }
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
    private static void appendGroup(final Document doc, final List<Group> selectedGroups, List<ResearchFile> selectedFiles, final Element parent, final Group group)
    {
        final Element groupElement = doc.createElement("group");
        groupElement.setAttribute("name", group.getName());

        for (final MetadataAssociation metadataAssociation : group.getMetadataAssociations())
        {
            appendMetadataAssociation(doc, groupElement, metadataAssociation);
        }

        for (final Group childGroup : group.getGroups())
        {
            if(selectedGroups.contains(childGroup))
            {
                appendGroup(doc, selectedGroups, selectedFiles, groupElement, childGroup);
            }
        }

        for (final ResearchFile childFile : group.getResearchFiles())
        {
            if(selectedFiles.contains(childFile))
            {
                appendResearchFile(doc, groupElement, childFile);
            }
        }

        parent.appendChild(groupElement);
    }
}
