package au.org.intersect.exsite9.xml;

import java.io.File;
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

import au.org.intersect.exsite9.domain.FieldOfResearch;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
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

    protected static Element createProjectInfo(final Document document, final Project project)
    {
        final Element projectInfoElement = document.createElement("project_info");
        projectInfoElement.setAttribute("identifier", project.getIdentifier());
        projectInfoElement.setAttribute("collectionType", project.getCollectionType());

        final Element nameElement = createElementWithTextContent(document, "projectName", project.getName());
        final Element ownerElement = createElementWithTextContent(document, "name", project.getOwner());
        final Element institutionElement = createElementWithTextContent(document, "institution", project.getInstitution());
        final Element emailElement = createElementWithTextContent(document, "email", project.getEmail());
        final Element descriptionElement = createElementWithTextContent(document, "description", project.getDescription());
        final Element rightsStatementElement = createElementWithTextContent(document, "rightsStatement", project.getRightsStatement());
        final Element accessRightsElement = createElementWithTextContent(document, "accessRights", project.getAccessRights());
        final Element licenseElement = createElementWithTextContent(document, "license", project.getLicence());
        final Element subjectElement = createElementWithTextContent(document, "subject", project.getSubject());
        final Element electronicLocationElement = createElementWithTextContent(document, "electronicLocation", project.getElectronicLocation());
        final Element physicalLocationElement = createElementWithTextContent(document, "physicalLocation", project.getPhysicalLocation());
        final Element placeOrRegionNameElement = createElementWithTextContent(document, "placeOrRegionName", project.getPlaceOrRegionName());
        final Element geographicalCoverageElement = createElementWithTextContent(document, "geographicalCoverage", project.getGeographicalCoverage());
        final Element datesOfCaptureElement = createElementWithTextContent(document, "datesOfCapture", project.getDatesOfCapture());
        final Element citationInformationElement = createElementWithTextContent(document, "citationInformation", project.getCitationInformation());
        final Element countriesElement = createElementWithTextContent(document, "countries", project.getCountries());
        final Element languagesElement = createElementWithTextContent(document, "languages", project.getLanguages());

        final FieldOfResearch fieldOfResearch = project.getFieldOfResearch();
        final Element fieldOfResearchElement = createElementWithTextContent(document, "fieldOfResearch", fieldOfResearch == null ? "" : fieldOfResearch.toString());

        final Element fundingBodyElement = createElementWithTextContent(document, "fundingBody", project.getFundingBody());
        final Element grantIDElement = createElementWithTextContent(document, "grantID", project.getGrantID());
        final Element relatedPartyElement = createElementWithTextContent(document, "relatedParty", project.getRelatedParty());
        final Element relatedGrantElement = createElementWithTextContent(document, "relatedGrant", project.getRelatedGrant());
        final Element relatedInformationElement = createElementWithTextContent(document, "relatedInformation", project.getRelatedInformation());

        projectInfoElement.appendChild(nameElement);
        projectInfoElement.appendChild(ownerElement);
        projectInfoElement.appendChild(institutionElement);
        projectInfoElement.appendChild(emailElement);
        projectInfoElement.appendChild(descriptionElement);
        projectInfoElement.appendChild(rightsStatementElement);
        projectInfoElement.appendChild(accessRightsElement);
        projectInfoElement.appendChild(licenseElement);
        projectInfoElement.appendChild(subjectElement);
        projectInfoElement.appendChild(electronicLocationElement);
        projectInfoElement.appendChild(physicalLocationElement);
        projectInfoElement.appendChild(placeOrRegionNameElement);
        projectInfoElement.appendChild(geographicalCoverageElement);
        projectInfoElement.appendChild(datesOfCaptureElement);
        projectInfoElement.appendChild(citationInformationElement);
        projectInfoElement.appendChild(countriesElement);
        projectInfoElement.appendChild(languagesElement);
        projectInfoElement.appendChild(fieldOfResearchElement);
        projectInfoElement.appendChild(fundingBodyElement);
        projectInfoElement.appendChild(grantIDElement);
        projectInfoElement.appendChild(relatedPartyElement);
        projectInfoElement.appendChild(relatedGrantElement);
        projectInfoElement.appendChild(relatedInformationElement);

        return projectInfoElement;
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
    
    protected static void appendMetadataAssociation(final Document doc, final Element parent, final MetadataAssociation metadataAssociation)
    {
        for (final MetadataValue metadataValue : metadataAssociation.getMetadataValues())
        {
            final MetadataCategory metadataCategory = metadataAssociation.getMetadataCategory();
            final Element metadataAssociationElement = doc.createElement(metadataCategory.getName());
            metadataAssociationElement.setTextContent(metadataValue.getValue());

            final MetadataAttributeValue attributeValue = metadataAssociation.getMetadataAttributeValue();
            if (attributeValue != null)
            {
                metadataAssociationElement.setAttribute(metadataCategory.getMetadataAttribute().getName(), attributeValue.getValue());
            }

            parent.appendChild(metadataAssociationElement);
        }
    }
    
    protected static void appendResearchFile(final Document doc, final Element parent, final ResearchFile researchFile, final boolean excludeMissingFiles)
    {
        final String researchFilePath = researchFile.getFile().getAbsolutePath();
        appendResearchFile(doc, parent, researchFile, researchFilePath, excludeMissingFiles);
    }

    protected static void appendResearchFile(final Document doc, final Element parent, final ResearchFile researchFile, final String researchFilePath, final boolean excludeMissingFiles)
    {
        final File file = researchFile.getFile();
        if (excludeMissingFiles && !file.exists())
        {
            return;
        }

        final Element researchFileElement = doc.createElement("file");

        final Element fileNameElement = doc.createElement("name");
        fileNameElement.setTextContent(file.getName());

        final Element filePathElement = doc.createElement("path");
        filePathElement.setTextContent(researchFilePath);

        researchFileElement.appendChild(fileNameElement);
        researchFileElement.appendChild(filePathElement);

        for (final MetadataAssociation metadataAssociation : researchFile.getMetadataAssociations())
        {
            appendMetadataAssociation(doc, researchFileElement, metadataAssociation);
        }
        parent.appendChild(researchFileElement);
    }

    protected static Element createElementWithTextContent(final Document document, final String elementName, final String textContent)
    {
        final Element element = document.createElement(elementName);
        element.setTextContent(textContent);
        return element;
    }
}
