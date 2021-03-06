/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

import au.org.intersect.exsite9.dto.ProjectFieldsDTO;

/**
 * Represents a Research Project
 */
@Entity
public final class Project implements Serializable
{
    private static final long serialVersionUID = 8533546987283338604L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(columnDefinition = "longvarchar")
    private String owner;

    @Column(columnDefinition = "longvarchar")
    private String institution;

    @Column(columnDefinition = "longvarchar")
    private String email;

    @Column(columnDefinition = "longvarchar")
    private String description;

    @Column(columnDefinition = "longvarchar")
    private String collectionType;

    @Column(columnDefinition = "longvarchar")
    private String rightsStatement;

    @Column(columnDefinition = "longvarchar")
    private String accessRights;

    @Column(columnDefinition = "longvarchar")
    private String licence;

    @Column(columnDefinition = "longvarchar")
    private String identifier;

    @Column(columnDefinition = "longvarchar")
    private String subject;

    @Column(columnDefinition = "longvarchar")
    private String electronicLocation;

    @Column(columnDefinition = "longvarchar")
    private String physicalLocation;

    @Column(columnDefinition = "longvarchar")
    private String placeOrRegionName;

    @Column(columnDefinition = "longvarchar")
    private String geographicalCoverage;

    @Column(columnDefinition = "longvarchar")
    private String datesOfCapture;

    @Column(columnDefinition = "longvarchar")
    private String citationInformation;

    @Column(columnDefinition = "longvarchar")
    private String countries;

    @Column(columnDefinition = "longvarchar")
    private String languages;

    @OneToOne
    private FieldOfResearch fieldOfResearch;

    @Column(columnDefinition = "longvarchar")
    private String fundingBody;

    @Column(columnDefinition = "longvarchar")
    private String grantID;

    @Column(columnDefinition = "longvarchar")
    private String relatedParty;

    @Column(columnDefinition = "longvarchar")
    private String relatedGrant;

    @Column(columnDefinition = "longvarchar")
    private String relatedInformation;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="project_folder", joinColumns={@JoinColumn(name="project_id", referencedColumnName="id")},
               inverseJoinColumns={@JoinColumn(name="folder_id", referencedColumnName="id")})
    private List<Folder> folders;

    @OneToOne(cascade = CascadeType.ALL)
    private Group rootNode;

    @OneToOne(cascade = CascadeType.ALL)
    private Group newFilesNode;

    @OneToOne(cascade = CascadeType.ALL)
    private Group excludedFilesNode;

    @OneToMany
    private final List<SubmissionPackage> submissionPackages = new ArrayList<SubmissionPackage>();

    @OneToOne
    private Schema schema;
    
    public Project()
    {
        name = "";
        owner = "";
        institution = "";
        email = "";
        description = "";
        collectionType = "";
        rightsStatement = "";
        accessRights = "";
        licence = "";
        identifier = "";
        subject = "";
        electronicLocation = "";
        physicalLocation = "";
        placeOrRegionName = "";
        geographicalCoverage = "";
        datesOfCapture = "";
        citationInformation = "";
        countries = "";
        languages = "";
        fundingBody = "";
        grantID = "";
        relatedParty = "";
        relatedGrant = "";
        relatedInformation = "";
    }

    public Project(final ProjectFieldsDTO projectFields)
    {
        this.name = projectFields.getName();
        this.owner = projectFields.getOwner();
        this.institution = projectFields.getInstitution();
        this.email = projectFields.getEmail();
        this.description = projectFields.getDescription();
        this.collectionType = projectFields.getCollectionType();
        this.rightsStatement = projectFields.getRightsStatement();
        this.accessRights = projectFields.getAccessRights();
        this.licence = projectFields.getLicence();
        this.identifier = projectFields.getIdentifier();
        this.subject = projectFields.getSubject();
        this.electronicLocation = projectFields.getElectronicLocation();
        this.physicalLocation = projectFields.getPhysicalLocation();
        this.placeOrRegionName = projectFields.getPlaceOrRegionName();
        this.geographicalCoverage = projectFields.getGeographicalCoverage();
        this.datesOfCapture = projectFields.getDatesOfCapture();
        this.citationInformation = projectFields.getCitationInformation();
        this.countries = projectFields.getCountries();
        this.languages = projectFields.getLanguages();
        this.fieldOfResearch = projectFields.getFieldOfResearch();
        this.fundingBody = projectFields.getFundingBody();
        this.grantID = projectFields.getGrantID();
        this.relatedParty = projectFields.getRelatedParty();
        this.relatedGrant = projectFields.getRelatedGrant();
        this.relatedInformation = projectFields.getRelatedInformation();
        this.folders = new ArrayList<Folder>(0);
        this.rootNode = new RootGroup(this.name);
        this.newFilesNode = new NewFilesGroup();
        this.excludedFilesNode = new ExcludedFilesGroup();
        this.rootNode.getGroups().add(newFilesNode);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOwner()
    {
        return owner;
    }
    
    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getInstitution()
    {
        return institution;
    }

    public void setInstitution(String institution)
    {
        this.institution = institution;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCollectionType()
    {
        return collectionType;
    }

    public void setCollectionType(String collectionType)
    {
        this.collectionType = collectionType;
    }

    public String getRightsStatement()
    {
        return rightsStatement;
    }

    public void setRightsStatement(String rightsStatement)
    {
        this.rightsStatement = rightsStatement;
    }

    public String getAccessRights()
    {
        return accessRights;
    }

    public void setAccessRights(String accessRights)
    {
        this.accessRights = accessRights;
    }

    public String getLicence()
    {
        return licence;
    }

    public void setLicence(String licence)
    {
        this.licence = licence;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getElectronicLocation()
    {
        return electronicLocation;
    }

    public void setElectronicLocation(String electronicLocation)
    {
        this.electronicLocation = electronicLocation;
    }

    public String getPhysicalLocation()
    {
        return physicalLocation;
    }

    public void setPhysicalLocation(String physicalLocation)
    {
        this.physicalLocation = physicalLocation;
    }

    public String getPlaceOrRegionName()
    {
        return placeOrRegionName;
    }

    public void setPlaceOrRegionName(String placeOrRegionName)
    {
        this.placeOrRegionName = placeOrRegionName;
    }

    public String getGeographicalCoverage()
    {
        return geographicalCoverage;
    }

    public void setGeographicalCoverage(String geographicalCoverage)
    {
        this.geographicalCoverage = geographicalCoverage;
    }

    public String getDatesOfCapture()
    {
        return datesOfCapture;
    }

    public void setDatesOfCapture(String datesOfCapture)
    {
        this.datesOfCapture = datesOfCapture;
    }

    public String getCitationInformation()
    {
        return citationInformation;
    }

    public void setCitationInformation(String citationInformation)
    {
        this.citationInformation = citationInformation;
    }

    public String getCountries()
    {
        return countries;
    }

    public void setCountries(final String countries)
    {
        this.countries = countries;
    }

    public String getLanguages()
    {
        return languages;
    }

    public void setLanguages(final String languages)
    {
        this.languages = languages;
    }

    public FieldOfResearch getFieldOfResearch()
    {
        return fieldOfResearch;
    }

    public void setFieldOfResearch(final FieldOfResearch fieldOfResearch)
    {
        this.fieldOfResearch = fieldOfResearch;
    }
    
    public String getFundingBody()
    {
        return fundingBody;
    }

    public void setFundingBody(final String fundingBody)
    {
        this.fundingBody = fundingBody;
    }

    public String getGrantID()
    {
        return grantID;
    }

    public void setGrantID(final String grantID)
    {
        this.grantID = grantID;
    }

    public String getRelatedParty()
    {
        return relatedParty;
    }

    public void setRelatedParty(String relatedParty)
    {
        this.relatedParty = relatedParty;
    }

    public String getRelatedGrant()
    {
        return relatedGrant;
    }

    public void setRelatedGrant(String relatedGrant)
    {
        this.relatedGrant = relatedGrant;
    }

    public String getRelatedInformation()
    {
        return relatedInformation;
    }

    public void setRelatedInformation(String relatedInformation)
    {
        this.relatedInformation = relatedInformation;
    }

    public Group getRootNode()
    {
        return rootNode;
    }

    public void setRootNode(Group rootNode)
    {
        this.rootNode = rootNode;
    }

    public Group getNewFilesNode()
    {
        return newFilesNode;
    }

    public void setNewFilesNode(Group newFilesNode)
    {
        this.newFilesNode = newFilesNode;
    }

    public Group getExcludedFilesNode()
    {
        return excludedFilesNode;
    }

    public void setExcludedFilesNode(Group excludedFilesNode)
    {
        this.excludedFilesNode = excludedFilesNode;
    }

    public List<Folder> getFolders()
    {
        return folders;
    }

    public void setFolders(List<Folder> folders)
    {
        this.folders = folders;
    }

    public List<SubmissionPackage> getSubmissionPackages()
    {
        return this.submissionPackages;
    }

    public Schema getSchema()
    {
        return this.schema;
    }

    public void setSchema(final Schema schema)
    {
        this.schema = schema;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof Project))
        {
            return false;
        }
        final Project other = (Project) obj;
        return Objects.equal(this.id, other.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        final ToStringBuilder tsb = new ToStringBuilder(this);
        tsb.append("id", this.id);
        tsb.append("name", this.name);
        return tsb.toString();
    }
}
