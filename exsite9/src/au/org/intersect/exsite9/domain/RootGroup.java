/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.domain;

import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;

/**
 * The Root group - every project has one.
 */
@Entity
public final class RootGroup extends Group
{
    private static final long serialVersionUID = -7294236885579971700L;
    
    public RootGroup()
    {
        super();
    }

    public RootGroup(final String projectName)
    {
        super(projectName);
    }
    

    /**
     * You can never add metadata associations to the RootGroup.
     * @{inheritDoc}
     */
    @Override
    public List<MetadataAssociation> getMetadataAssociations()
    {
        return Collections.unmodifiableList(Collections.<MetadataAssociation>emptyList());
    }

}
