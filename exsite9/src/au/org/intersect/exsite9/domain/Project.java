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

    private String name;
    private String owner;
    private String description;
    private String collectionType;
    private String rightsStatement;
    private String accessRights;
    private String licence;
    private String identifier;
    private String subject;
    private String electronicLocation;
    private String physicalLocation;
    private String placeOrRegionName;
    private String latitudeLongitude;
    private String datesOfCapture;
    private String citationInformation;
    private String relatedParty;
    private String relatedActivity;
    private String relatedInformation;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="project_folder", joinColumns={@JoinColumn(name="project_id", referencedColumnName="id")},
               inverseJoinColumns={@JoinColumn(name="folder_id", referencedColumnName="id")})
    private List<Folder> folders;

    @OneToMany
    private List<MetadataCategory> metadataCategories;

    @OneToOne(cascade = CascadeType.ALL)
    private Group rootNode;

    @OneToOne(cascade = CascadeType.ALL)
    private Group newFilesNode;

    @OneToMany
    private final List<SubmissionPackage> submissionPackages = new ArrayList<SubmissionPackage>();
    
    public Project()
    {
        name = "";
        owner = "";
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
        latitudeLongitude = "";
        datesOfCapture = "";
        citationInformation = "";
        relatedParty = "";
        relatedActivity = "";
        relatedInformation = "";
    }

    public Project(final ProjectFieldsDTO projectFields)
    {
        this.name = projectFields.getName();
        this.description = projectFields.getDescription();
        this.owner = projectFields.getOwner();
        this.collectionType = projectFields.getCollectionType();
        this.rightsStatement = projectFields.getRightsStatement();
        this.accessRights = projectFields.getAccessRights();
        this.licence = projectFields.getLicence();
        this.identifier = projectFields.getIdentifier();
        this.subject = projectFields.getSubject();
        this.electronicLocation = projectFields.getElectronicLocation();
        this.physicalLocation = projectFields.getPhysicalLocation();
        this.placeOrRegionName = projectFields.getPlaceOrRegionName();
        this.latitudeLongitude = projectFields.getLatitudeLongitude();
        this.datesOfCapture = projectFields.getDatesOfCapture();
        this.citationInformation = projectFields.getCitationInformation();
        this.relatedParty = projectFields.getRelatedParty();
        this.relatedActivity = projectFields.getRelatedActivity();
        this.relatedInformation = projectFields.getRelatedInformation();

        this.folders = new ArrayList<Folder>(0);
        this.rootNode = new Group(this.name);
        this.newFilesNode = new NewFilesGroup();
        this.rootNode.getGroups().add(newFilesNode);
        this.metadataCategories = new ArrayList<MetadataCategory>();
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
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

    public String getLatitudeLongitude()
    {
        return latitudeLongitude;
    }

    public void setLatitudeLongitude(String latitudeLongitude)
    {
        this.latitudeLongitude = latitudeLongitude;
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

    public String getRelatedParty()
    {
        return relatedParty;
    }

    public void setRelatedParty(String relatedParty)
    {
        this.relatedParty = relatedParty;
    }

    public String getRelatedActivity()
    {
        return relatedActivity;
    }

    public void setRelatedActivity(String relatedActivity)
    {
        this.relatedActivity = relatedActivity;
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

    public List<Folder> getFolders()
    {
        return folders;
    }

    public void setFolders(List<Folder> folders)
    {
        this.folders = folders;
    }

    public List<MetadataCategory> getMetadataCategories()
    {
        return this.metadataCategories;
    }

    public void setMetadataCategories(final List<MetadataCategory> mdcs)
    {
        this.metadataCategories = mdcs;
    }

    public List<SubmissionPackage> getSubmissionPackages()
    {
        return this.submissionPackages;
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
