/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dto;

public class ProjectFieldsDTO
{
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

    public ProjectFieldsDTO(final String name, final String owner, final String description,
            final String collectionType, final String rightsStatement, final String accessRights, final String licence,
            final String identifier, final String subject, final String electronicLocation,
            final String physicalLocation, final String placeOrRegionName, final String latitudeLongitude,
            final String datesOfCapture, final String citationInformation, final String relatedParty,
            final String relatedActivity, final String relatedInformation)
    {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.collectionType = collectionType;
        this.rightsStatement = rightsStatement;
        this.accessRights = accessRights;
        this.licence = licence;
        this.identifier = identifier;
        this.subject = subject;
        this.electronicLocation = electronicLocation;
        this.physicalLocation = physicalLocation;
        this.placeOrRegionName = placeOrRegionName;
        this.latitudeLongitude = latitudeLongitude;
        this.datesOfCapture = datesOfCapture;
        this.citationInformation = citationInformation;
        this.relatedParty = relatedParty;
        this.relatedActivity = relatedActivity;
        this.relatedInformation = relatedInformation;
    }

    public String getName()
    {
        return name;
    }

    public String getOwner()
    {
        return owner;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCollectionType()
    {
        return collectionType;
    }

    public String getRightsStatement()
    {
        return rightsStatement;
    }

    public String getAccessRights()
    {
        return accessRights;
    }

    public String getLicence()
    {
        return licence;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public String getSubject()
    {
        return subject;
    }

    public String getElectronicLocation()
    {
        return electronicLocation;
    }

    public String getPhysicalLocation()
    {
        return physicalLocation;
    }

    public String getPlaceOrRegionName()
    {
        return placeOrRegionName;
    }

    public String getLatitudeLongitude()
    {
        return latitudeLongitude;
    }

    public String getDatesOfCapture()
    {
        return datesOfCapture;
    }

    public String getCitationInformation()
    {
        return citationInformation;
    }

    public String getRelatedParty()
    {
        return relatedParty;
    }

    public String getRelatedActivity()
    {
        return relatedActivity;
    }

    public String getRelatedInformation()
    {
        return relatedInformation;
    }
}
