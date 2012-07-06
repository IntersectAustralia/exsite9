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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;

public class BaseXMLBuilder
{

    protected static Document createNewDocument() throws ParserConfigurationException
    {
        final DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
        
        return documentBuilder.newDocument();
    }

    protected static Element createProjectRootElement(Document doc, Project project)
    {
        final Element rootElement = doc.createElement("project");
        
        rootElement.setAttribute("name", project.getName());
        rootElement.setAttribute("owner", project.getOwner());
        rootElement.setAttribute("description", project.getDescription());
        rootElement.setAttribute("collectionType", project.getCollectionType());
        rootElement.setAttribute("rightsStatement", project.getRightsStatement());
        rootElement.setAttribute("accessRights", project.getAccessRights());
        rootElement.setAttribute("licence", project.getLicence());
        rootElement.setAttribute("identifier", project.getIdentifier());
        rootElement.setAttribute("subject", project.getSubject());
        rootElement.setAttribute("electronicLocation", project.getElectronicLocation());
        rootElement.setAttribute("physicalLocation", project.getPhysicalLocation());
        rootElement.setAttribute("placeOrRegionName", project.getPlaceOrRegionName());
        rootElement.setAttribute("latitudeLongitude", project.getLatitudeLongitude());
        rootElement.setAttribute("datesOfCapture", project.getDatesOfCapture());
        rootElement.setAttribute("citationInformation", project.getCitationInformation());
        rootElement.setAttribute("relatedParty", project.getRelatedParty());
        rootElement.setAttribute("relatedActivity", project.getRelatedActivity());
        rootElement.setAttribute("relatedInformation", project.getRelatedInformation());
        
        return rootElement;
    }
    
    protected static String transformDocumentToString(Document doc) throws TransformerConfigurationException, TransformerException
    {
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
    
    protected static void appendMetadataAssociation(final Document doc, final Element parent,
            final MetadataAssociation metadataAssociation)
    {
        for (final MetadataValue metadataValue : metadataAssociation.getMetadataValues())
        {
            final Element metadataAssociationElement = doc.createElement("metadata");
            metadataAssociationElement.setAttribute("category", metadataAssociation.getMetadataCategory().getName());
            metadataAssociationElement.setAttribute("value", metadataValue.getValue());
            parent.appendChild(metadataAssociationElement);
        }
    }
    
    protected static void appendResearchFile(final Document doc, final Element parent, final ResearchFile researchFile)
    {
        final Element researchFileElement = doc.createElement("file");
        researchFileElement.setAttribute("name", researchFile.getFile().getName());
        final Element filePathElement = doc.createElement("path");
        filePathElement.setTextContent(researchFile.getFile().getAbsolutePath());
        researchFileElement.appendChild(filePathElement);
        for (final MetadataAssociation metadataAssociation : researchFile.getMetadataAssociations())
        {
            appendMetadataAssociation(doc, researchFileElement, metadataAssociation);
        }
        parent.appendChild(researchFileElement);
    }
}
