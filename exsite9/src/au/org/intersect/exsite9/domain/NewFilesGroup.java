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
 * Used only to tag the New Files group of a project, so we can easily identify this group with instanceof checks.
 */
@Entity
public final class NewFilesGroup extends Group
{
    /**
     * Generated UID
     */
    private static final long serialVersionUID = 4593179511641620404L;

    public NewFilesGroup()
    {
        super("New Files");
    }

    /**
     * You can never add groups to the NewFilesGroup.
     * @{inheritDoc}
     */
    @Override
    public List<Group> getGroups()
    {
        return Collections.unmodifiableList(Collections.<Group>emptyList());
    }

    /**
     * You can never add metadata associations to the NewFilesGroup.
     * @{inheritDoc}
     */
    @Override
    public List<MetadataAssociation> getMetadataAssociations()
    {
        return Collections.unmodifiableList(Collections.<MetadataAssociation>emptyList());
    }
}
