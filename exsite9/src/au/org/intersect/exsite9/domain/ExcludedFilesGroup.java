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
 * This group is a holder for files that have been excluded from the project.
 */
@Entity
public final class ExcludedFilesGroup extends Group
{
    public static final String NAME = "Excluded Files";
    private static final long serialVersionUID = -5078733345256805564L;

    public ExcludedFilesGroup()
    {
        super(NAME);
    }

    /**
     * You can never add groups to the ExcludedFilesGroup.
     * @{inheritDoc}
     */
    @Override
    public List<Group> getGroups()
    {
        return Collections.unmodifiableList(Collections.<Group>emptyList());
    }

    /**
     * You can never add metadata associations to the ExcludedFilesGroup.
     * @{inheritDoc}
     */
    @Override
    public List<MetadataAssociation> getMetadataAssociations()
    {
        return Collections.unmodifiableList(Collections.<MetadataAssociation>emptyList());
    }
}
