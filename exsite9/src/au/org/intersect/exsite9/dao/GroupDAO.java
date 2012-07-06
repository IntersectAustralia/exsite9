/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.ResearchFile;

public final class GroupDAO
{
    private EntityManager em;
    private static final Logger LOG = Logger.getLogger(GroupDAO.class);

    public GroupDAO(final EntityManager entityManager)
    {
        this.em = entityManager;
    }

    public void createGroup(final Group group)
    {
        em.getTransaction().begin();
        em.persist(group);
        em.getTransaction().commit();
    }

    public void deleteGroup(final Group group)
    {
        final boolean localTransaction = em.getTransaction().isActive();
        if (!localTransaction)
        {
            em.getTransaction().begin();
        }
        em.remove(em.merge(group));
        if (!localTransaction)
        {
            em.getTransaction().commit();
        }
    }

    public Group updateGroup(final Group group)
    {
        boolean localTransaction = (em.getTransaction().isActive()) ? false : true;
        
        if (localTransaction)
        {
            em.getTransaction().begin();
        }
        final Group updatedGroup = em.merge(group);
        if(localTransaction)
        {
            em.getTransaction().commit();
        }
        return updatedGroup;
    }
    
    public Group findById(final Long id)
    {
        return em.find(Group.class, id);
    }

    public Group getParent(final Group group)
    {
        final TypedQuery<Group> query = em.createQuery("SELECT g FROM Group g WHERE :child MEMBER OF g.groups", Group.class);
        query.setParameter("child", group);

        final List<Group> results = query.getResultList();

        if (results.size() != 1)
        {
            LOG.error("A Group has multiple parents, or does not have a parent.");
            return null;
        }
        return results.get(0);
    }

    public List<Group> getGroupsWithAssociatedMetadata(final MetadataCategory metadataCategory, final MetadataValue metadataValue)
    {
        final String queryJQL = "SELECT g FROM Group g JOIN g.metadataAssociations a WHERE a.metadataCategory = :category AND :value MEMBER OF a.metadataValues";
        final TypedQuery<Group> query = em.createQuery(queryJQL, Group.class);
        query.setParameter("category", metadataCategory);
        query.setParameter("value", metadataValue);
        return query.getResultList();
    }
    
    public Group getParent(final ResearchFile researchFile)
    {
        final TypedQuery<Group> query = em.createQuery("SELECT g FROM Group g WHERE :child MEMBER OF g.researchFiles", Group.class);
        query.setParameter("child", researchFile);

        final List<Group> results = query.getResultList();

        if (results.size() != 1)
        {
            LOG.error("A Research File has multiple parents, or does not have a parent: " + researchFile);
            return null;
        }
        return results.get(0);
    }
    
    /**
     * Returns the list of groups in the hierarchy between the root group
     * and the files in the list of selected files (ie it includes the groups that contain
     * a selected file, and the groups that contain those groups, and the groups that
     * contain those groups, etc)
     * @param selectedFiles The list of selected files
     * @return The list of groups
     */
    public List<Group> getGroupsContainingSelectedFiles(final List<ResearchFile> selectedFiles)
    {
        List<Group> groups = new ArrayList<Group>(0);
        
        for(final ResearchFile file : selectedFiles)
        {
            Group group = getParent(file);
            
            while(group != null)
            {
                if (group.getResearchFiles().contains(file))
                {
                    if(groups.contains(group))
                    {
                        break;
                    }
                    else
                    {
                        groups.add(group);
                        
                        Group parentGroup = getParent(group);
                        
                        while(parentGroup != null)
                        {
                            if (groups.contains(parentGroup))
                            {
                                break;
                            }
                            else
                            {
                                groups.add(parentGroup);
                                parentGroup = getParent(parentGroup);
                            }
                        }
                    }
                }
                group = getParent(group);
            }
        }
        return groups;
    }

}
