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
import au.org.intersect.exsite9.domain.SubmissionPackage;

public class SIPXMLBuilder extends BaseXMLBuilder
{
    private static final Logger LOG = Logger.getLogger(SIPXMLBuilder.class);

    /**
     * Builds XML for a SIP for the provided SubmissionPackage. 
     * @param project The Project the submission package is contained within.
     * @param selectedGroups The Groups that contain files are in the SubmissionPackage.
     * @param submissionPackage The submission package.
     * @param useGroupPaths {@code true} to use the path of a file relative to its group. {@code false} to use the absolute path of a file.
     * @return The XML.
     */
    public static String buildXML(final Project project, final List<Group> selectedGroups, final SubmissionPackage submissionPackage, final boolean useGroupPaths)
    {
        final List<ResearchFile> selectedFiles = submissionPackage.getResearchFiles();
        try
        {
            final Document doc = createNewDocument();
            
            final Element rootElement = createProjectRootElement(doc, project);
            doc.appendChild(rootElement);

            for (final Group group : project.getRootNode().getGroups())
            {
                if(selectedGroups.contains(group))
                {
                    appendGroup(doc, selectedGroups, selectedFiles, rootElement, group, useGroupPaths, "");
                }
            }

            for (final ResearchFile researchFile : project.getRootNode().getResearchFiles())
            {
                if(selectedFiles.contains(researchFile))
                {
                    if (useGroupPaths)
                    {
                        appendResearchFile(doc, rootElement, researchFile, researchFile.getFile().getName(), true);
                    }
                    else
                    {
                        appendResearchFile(doc, rootElement, researchFile, true);
                    }
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
    private static void appendGroup(final Document doc, final List<Group> selectedGroups, List<ResearchFile> selectedFiles, final Element parent, final Group group, final boolean useGroupPaths, final String parentGroupPath)
    {
        final Element groupElement = doc.createElement("group");
        groupElement.setAttribute("name", group.getName());
        final String groupPath = parentGroupPath + group.getName() + "/";
        
        for (final MetadataAssociation metadataAssociation : group.getMetadataAssociations())
        {
            appendMetadataAssociation(doc, groupElement, metadataAssociation);
        }

        for (final Group childGroup : group.getGroups())
        {
            if(selectedGroups.contains(childGroup))
            {
                appendGroup(doc, selectedGroups, selectedFiles, groupElement, childGroup, useGroupPaths, groupPath);
            }
        }

        for (final ResearchFile childFile : group.getResearchFiles())
        {
            if(selectedFiles.contains(childFile))
            {
                if (useGroupPaths)
                {
                    appendResearchFile(doc, groupElement, childFile, groupPath + childFile.getFile().getName(), true);
                }
                else
                {
                    appendResearchFile(doc, groupElement, childFile, true);
                }
            }
        }

        parent.appendChild(groupElement);
    }
}
