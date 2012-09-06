/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dto;

import au.org.intersect.exsite9.domain.FieldOfResearch;

public class ProjectFieldsDTO
{
    private final String name;
    private final String owner;
    private final String institution;
    private final String email;
    private final String description;
    private final String collectionType;
    private final String rightsStatement;
    private final String accessRights;
    private final String licence;
    private final String identifier;
    private final String subject;
    private final String electronicLocation;
    private final String physicalLocation;
    private final String placeOrRegionName;
    private final String geograhpicalCoverage;
    private final String datesOfCapture;
    private final String citationInformation;
    private final String countries;
    private final String languages;
    private final FieldOfResearch fieldOfResearch;
    private final String fundingBody;
    private final String grantID;
    private final String relatedParty;
    private final String relatedGrant;
    private final String relatedInformation;

    public ProjectFieldsDTO(final String name, final String owner, final String institution, final String email,
                            final String description, final String collectionType, final String rightsStatement,
                            final String accessRights, final String licence, final String identifier, final String subject,
                            final String electronicLocation, final String physicalLocation, final String placeOrRegionName,
                            final String geographicalCoverage, final String datesOfCapture, final String citationInformation,
                            final String countries, final String languages, final FieldOfResearch fieldOfResearch,
                            final String fundingBody, final String grantID, final String relatedParty, final String relatedGrant,
                            final String relatedInformation)
    {
        this.name = name;
        this.owner = owner;
        this.institution = institution;
        this.email = email;
        this.description = description;
        this.collectionType = collectionType;
        this.rightsStatement = rightsStatement;
        this.accessRights = accessRights;
        this.licence = licence;
        this.identifier = identifier;
        this.subject = subject;
        this.electronicLocation = electronicLocation;
        this.physicalLocation = physicalLocation;
        this.placeOrRegionName = placeOrRegionName;
        this.geograhpicalCoverage = geographicalCoverage;
        this.datesOfCapture = datesOfCapture;
        this.citationInformation = citationInformation;
        this.countries = countries;
        this.languages = languages;
        this.fieldOfResearch = fieldOfResearch;
        this.fundingBody = fundingBody;
        this.grantID = grantID;
        this.relatedParty = relatedParty;
        this.relatedGrant = relatedGrant;
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

    public String getInstitution()
    {
        return institution;
    }

    public String getEmail()
    {
        return email;
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

    public String getGeographicalCoverage()
    {
        return geograhpicalCoverage;
    }

    public String getDatesOfCapture()
    {
        return datesOfCapture;
    }

    public String getCitationInformation()
    {
        return citationInformation;
    }

    public String getCountries()
    {
        return countries;
    }

    public String getLanguages()
    {
        return languages;
    }

    public FieldOfResearch getFieldOfResearch()
    {
        return fieldOfResearch;
    }

    public String getFundingBody()
    {
        return fundingBody;
    }

    public String getGrantID()
    {
        return grantID;
    }

    public String getRelatedParty()
    {
        return relatedParty;
    }

    public String getRelatedGrant()
    {
        return relatedGrant;
    }

    public String getRelatedInformation()
    {
        return relatedInformation;
    }
}
