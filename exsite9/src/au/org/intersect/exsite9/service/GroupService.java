/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Logger;

import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.dao.MetadataAssociationDAO;
import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.dao.factory.MetadataAssociationDAOFactory;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAssociation;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;

/**
 * 
 */
public final class GroupService implements IGroupService
{
    private static final Logger LOG = Logger.getLogger(GroupService.class);

    private final EntityManagerFactory entityManagerFactory;
    private final GroupDAOFactory groupDAOFactory;
    private final MetadataAssociationDAOFactory metadataAssociationDAOFactory;
    private final ResearchFileDAOFactory researchFileDAOFactory;

    public GroupService(final EntityManagerFactory entityManagerFactory,
                        final GroupDAOFactory groupDAOFactory,
                        final MetadataAssociationDAOFactory metadataAssociationDAOFactory,
                        final ResearchFileDAOFactory researchFileDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.groupDAOFactory = groupDAOFactory;
        this.metadataAssociationDAOFactory = metadataAssociationDAOFactory;
        this.researchFileDAOFactory = researchFileDAOFactory;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Group createNewGroup(final String groupName)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            final Group group = new Group(groupName);
            groupDAO.createGroup(group);
            return group;
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void deleteGroup(final Group groupToDelete)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            final MetadataAssociationDAO metadataDAO = metadataAssociationDAOFactory.createInstance(em);
            final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);

            final Group parentGroup = groupToDelete.getParentGroup();

            parentGroup.getGroups().remove(groupToDelete);
            parentGroup.getGroups().addAll(groupToDelete.getGroups());
            final List<ResearchFile> childResearchFiles = groupToDelete.getResearchFiles();
            for (final ResearchFile researchFile : childResearchFiles)
            {
                researchFile.setParentGroup(parentGroup);
                researchFileDAO.updateResearchFile(researchFile);
            }
            parentGroup.getResearchFiles().addAll(childResearchFiles);

            groupToDelete.getGroups().clear();
            childResearchFiles.clear();

            groupDAO.updateGroup(parentGroup);
            groupDAO.deleteGroup(groupToDelete);

            // Remove metadata associations
            for (final MetadataAssociation metadataAssociation : groupToDelete.getMetadataAssociations())
            {
                metadataDAO.removeMetadataAssociation(metadataAssociation);
            }
        }
        finally
        {
            em.close();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void addChildGroup(final Group parentGroup, final Group childGroup)
    {
        EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            parentGroup.getGroups().add(childGroup);
            childGroup.setParentGroup(parentGroup);
            groupDAO.updateGroup(childGroup);
            groupDAO.updateGroup(parentGroup);
        }
        finally
        {
            em.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performHierarchyMove(List<HierarchyMoveDTO> moveList)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
        final ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
        try
        {
            for(HierarchyMoveDTO moveDTO : moveList)
            {
                Group oldParent;
                if (moveDTO.getOldParent() instanceof Project)
                {
                    oldParent = ((Project)moveDTO.getOldParent()).getRootNode();
                }
                else
                {
                    oldParent = (Group) moveDTO.getOldParent();
                }
                
                Group newParent;
                if(moveDTO.getNewParent() instanceof Project)
                {
                    newParent = ((Project)moveDTO.getNewParent()).getRootNode();
                }
                else
                {
                    newParent = (Group) moveDTO.getNewParent();
                }
                
                Object childObj = moveDTO.getChild();
                
                boolean movedOK = false;
                
                if (childObj instanceof Group)
                {
                    final Group group = (Group) childObj;
                    movedOK = moveGroupToNewGroup((Group) childObj,oldParent,newParent);
                    groupDAO.updateGroup(group);
                }
                else if (childObj instanceof ResearchFile)
                {
                    final ResearchFile rf = (ResearchFile) childObj;
                    movedOK = moveFileToNewGroup(rf, oldParent, newParent);
                    researchFileDAO.updateResearchFile(rf);
                }
                else
                {
                    // TODO: Log this?
                    continue;
                }
                
                if(movedOK)
                {
                    em.getTransaction().begin();
                    groupDAO.updateGroup(oldParent);
                    groupDAO.updateGroup(newParent);
                    em.getTransaction().commit();
                }
            }
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void associateMetadata(final Group group, final MetadataCategory metadataCategory, final MetadataValue metadataValue)
    {
        LOG.info("Assosciating metadata with group. " + group + " " + metadataCategory + " " + metadataValue);

        final EntityManager em = this.entityManagerFactory.createEntityManager();
        try
        {
            final List<MetadataAssociation> existingAssociations = group.getMetadataAssociations();

            final MetadataAssociationDAO metadataAssociationDAO = this.metadataAssociationDAOFactory.createInstance(em);
    
            boolean addedAssociation = false;
            for (final MetadataAssociation existingAssociation : existingAssociations)
            {
                if (existingAssociation.getMetadataCategory().equals(metadataCategory))
                {
                    if (existingAssociation.getMetadataValues().contains(metadataValue))
                    {
                        // nothing to do!
                        return;
                    }
                    existingAssociation.getMetadataValues().add(metadataValue);
                    metadataAssociationDAO.updateMetadataAssociation(existingAssociation);
                    addedAssociation = true;
                }
            }

            if (!addedAssociation)
            {
                final MetadataAssociation metadataAssociation = new MetadataAssociation(metadataCategory);
                metadataAssociation.getMetadataValues().add(metadataValue);
                metadataAssociationDAO.createMetadataAssociation(metadataAssociation);
                group.getMetadataAssociations().add(metadataAssociation);
            }

            final GroupDAO groupDAO = this.groupDAOFactory.createInstance(em);
            groupDAO.updateGroup(group);

        }
        finally
        {
            em.close();
        }
    }

    @Override
    public void disassociateMetadata(final Group group, final MetadataCategory metadataCategory, final MetadataValue metadataValue)
    {
        LOG.info("Disassosciating metadata from group. " + group + " " + metadataCategory + " " + metadataValue);

        final List<MetadataAssociation> existingAssociations = group.getMetadataAssociations();

        for (final MetadataAssociation existingAssociation : existingAssociations)
        {
            if (existingAssociation.getMetadataCategory().equals(metadataCategory))
            {
                if (existingAssociation.getMetadataValues().remove(metadataValue))
                {
                    final EntityManager em = this.entityManagerFactory.createEntityManager();
                    try
                    {
                        final MetadataAssociationDAO metadataAssociationDAO = this.metadataAssociationDAOFactory.createInstance(em);
                        metadataAssociationDAO.updateMetadataAssociation(existingAssociation);

                        if (existingAssociation.getMetadataValues().isEmpty())
                        {
                            existingAssociations.remove(existingAssociation);

                            final GroupDAO groupDAO = this.groupDAOFactory.createInstance(em);
                            groupDAO.updateGroup(group);

                            metadataAssociationDAO.removeMetadataAssociation(existingAssociation);
                        }
                        return;
                    }
                    finally
                    {
                        em.close();
                    }
                }
            }
        }
    }

    private boolean moveGroupToNewGroup(Group child, Group oldParent, Group newParent)
    {
        if(oldParent.getGroups().remove(child))
        {
            if(newParent.getGroups().add(child))
            {
                child.setParentGroup(newParent);
                return true;
            }
        }
        return false;
    }
    
    private boolean moveFileToNewGroup(ResearchFile child, Group oldParent, Group newParent)
    {
        if(oldParent.getResearchFiles().remove(child))
        {
            if(newParent.getResearchFiles().add(child))
            {
                child.setParentGroup(newParent);
                return true;
            }
        }
        return false;
    }

    @Override
    public void renameGroup(final Group groupToBeRenamed, final String newName)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            groupToBeRenamed.setName(newName);
            groupDAO.updateGroup(groupToBeRenamed);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public List<Group> getGroupsWithAssociatedMetadata(final MetadataCategory metadataCategory, final MetadataValue metadataValue)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            return groupDAO.getGroupsWithAssociatedMetadata(metadataCategory, metadataValue);
        }
        finally
        {
            em.close();
        }
    }

    @Override
    public Group findGroupByID(final Long groupID)
    {
        final EntityManager em = entityManagerFactory.createEntityManager();
        try
        {
            final GroupDAO gropDAO = groupDAOFactory.createInstance(em);
            return gropDAO.findById(groupID);
        }
        finally
        {
            em.close();
        }
    }
}
