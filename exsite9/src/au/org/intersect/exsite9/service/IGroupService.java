/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFileSortField;
import au.org.intersect.exsite9.domain.SortFieldDirection;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;

/**
 * A Service for performing actions to Groups.
 */
public interface IGroupService
{
    /**
     * Creates a new group.
     * @param groupName The name of the new group to created.
     * @param project the current project
     * @return The newly created Group.
     */
    Group createNewGroup(final String groupName, final Project project);

    /**
     * Deletes a group. Moves all the child groups and files to the provided group's parent.
     * @param groupToDelete The group to delete.
     */
    void deleteGroup(final Group groupToDelete);

    /**
     * Checks if it is safe to delete a group. Should be called before {@link IGroupService#deleteGroup(Group)}.
     * @param groupToDelete The group to check.
     * @return {@code null} if the provided group can be safely deleted, or an error message indicating why it cannot be safely deleted.
     */
    String deleteGroupCheck(final Group groupToDelete);
    
    /**
     * Renames a group.
     * @param groupToBeRenamed The group to renamed.
     * @param NewName The new group name.
     */
    void renameGroup(final Group groupToBeRenamed, final String NewName);

    /**
     * Adds a group to another group.
     * @param parentGroup The group that will contain the child group.
     * @param childGroup The group that will be added.
     */
    void addChildGroup(final Group parentGroup, final Group childGroup);
    
    /**
     * Moves selected group members to a new group
     * @param moveList The list of items to move
     * @return {@code null} if successful, or a reason why the move was not successful.
     */
    String performHierarchyMove(final List<HierarchyMoveDTO> moveList);

    /**
     * Associates a metadata category and value to a Group.
     * @param group The group to associate metadata to.
     * @param metadataCategory The category of metadata to associate.
     * @param metadataVale The value of metadata to associate.
     */
    void associateMetadata(final Group group, final MetadataCategory metadataCategory, final MetadataValue metadataValue, final MetadataAttributeValue metadataAttributeValue);

    /**
     * Diassociates a metadata category and value from a Group.
     * @param group The group to disassociate metadata from.
     * @param metadataCategory The category of metadata to disassociate.
     * @param metadataValue The value of metadata to disassociate.
     */
    void disassociateMetadata(final Group group, final MetadataCategory metadataCategory, final MetadataValue metadataValue);

    void disassociateMetadataAttributeValue(final MetadataCategory metadataCategory, final MetadataAttributeValue metadataVale);

    void disassociateMultipleMetadataValues(final Group group, final MetadataCategory metadataCategory, final List<MetadataValue> metadataValues);

    /**
     * Determines which groups are associated with a metadata category/value pair.
     * @param metadataCategory The metadata category.
     * @param metadataVale The metadata value.
     * @return A list of group associated with a metadata category/value pair.
     */
    List<Group> getGroupsWithAssociatedMetadata(final MetadataCategory metadataCategory, final MetadataValue metadataValue);

    List<Group> getGroupsWithAssociatedMetadataAttribute(final MetadataCategory metadataCategory, final MetadataAttributeValue metadataAttributeValue);

    /**
     * Obtains a group by its ID.
     * @param groupID The ID of the group to get.
     * @return The group, or {@code null} if it could not be found.
     */
    Group findGroupByID(final Long groupID);

    /**
     * Configures a group's research files to be sorted by a particular field.
     * @param group The group to configure the sort on.
     * @param sortField The field to sort by.
     * @param sortDirection The direction to sort by.
     */
    void sortResearchFilesInGroup(final Group group, final ResearchFileSortField sortField, final SortFieldDirection sortDirection);
}
