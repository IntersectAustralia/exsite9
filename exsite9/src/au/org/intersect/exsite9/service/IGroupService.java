/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;

/**
 * A Service for performing actions to Groups.
 */
public interface IGroupService
{
    /**
     * Creates a new group.
     * @param groupName The name of the new group to created.
     * @return The newly created Group.
     */
    Group createNewGroup(final String groupName);

    /**
     * Adds a group to another group.
     * @param parentGroup The group that will contain the child group.
     * @param childGroup The group that will be added.
     */
    void addChildGroup(final Group parentGroup, final Group childGroup);
    
    /**
     * Moves selected group members to a new group
     * @param moveList The list of items to move
     */
    void performHierarchyMove(final List<HierarchyMoveDTO> moveList);
}
