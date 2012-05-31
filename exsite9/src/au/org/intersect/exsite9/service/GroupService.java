/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.domain.Group;

/**
 * 
 */
public final class GroupService implements IGroupService
{
    private final GroupDAO groupDAO;

    public GroupService(final GroupDAO groupDAO)
    {
        this.groupDAO = groupDAO;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Group createNewGroup(final String groupName)
    {
        final Group group = new Group(groupName);
        this.groupDAO.createGroup(group);
        return group;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addChildGroup(final Group parentGroup, final Group childGroup)
    {
        parentGroup.getGroups().add(childGroup);
        this.groupDAO.updateGroup(parentGroup);
    }
}
