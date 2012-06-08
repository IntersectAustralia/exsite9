/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.service;

import java.util.List;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.dao.GroupDAO;
import au.org.intersect.exsite9.dao.factory.GroupDAOFactory;
import au.org.intersect.exsite9.database.ExSite9EntityManagerFactory;
import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.Project;
import au.org.intersect.exsite9.domain.ResearchFile;
import au.org.intersect.exsite9.dto.HierarchyMoveDTO;

/**
 * 
 */
public final class GroupService implements IGroupService
{
    private final ExSite9EntityManagerFactory entityManagerFactory;
    private final GroupDAOFactory groupDAOFactory;

    public GroupService(final ExSite9EntityManagerFactory entityManagerFactory,
                        final GroupDAOFactory groupDAOFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.groupDAOFactory = groupDAOFactory;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Group createNewGroup(final String groupName)
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            GroupDAO groupDAO = groupDAOFactory.createInstance(em);
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
    public void addChildGroup(final Group parentGroup, final Group childGroup)
    {
        EntityManager em = entityManagerFactory.getEntityManager();
        try
        {
            GroupDAO groupDAO = groupDAOFactory.createInstance(em);
            parentGroup.getGroups().add(childGroup);
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
        EntityManager em = entityManagerFactory.getEntityManager();
        GroupDAO groupDAO = groupDAOFactory.createInstance(em);
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
                    movedOK = moveGroupToNewGroup((Group) childObj,oldParent,newParent);
                }
                else if (childObj instanceof ResearchFile)
                {
                    movedOK = moveFileToNewGroup((ResearchFile) childObj,oldParent,newParent);
                }
                else
                {
                    // TODO: Log this?
                    continue;
                }
                
                if(movedOK)
                {
                    groupDAO.updateGroup(oldParent);
                    groupDAO.updateGroup(newParent);
                }
            }
        }
        finally
        {
            em.close();
        }
    }

    private boolean moveGroupToNewGroup(Group child, Group oldParent, Group newParent)
    {
        if(oldParent.getGroups().remove(child))
        {
            if(newParent.getGroups().add(child))
            {
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
                return true;
            }
        }
        return false;
    }
}
