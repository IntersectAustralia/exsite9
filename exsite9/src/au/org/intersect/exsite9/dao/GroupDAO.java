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

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.MetadataAttributeValue;
import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;
import au.org.intersect.exsite9.domain.ResearchFile;

/**
 * Data Access Object for {@link Group} instances
 */
public final class GroupDAO
{
    private EntityManager em;

    public GroupDAO(final EntityManager entityManager)
    {
        this.em = entityManager;
    }

    public void createGroup(final Group group)
    {
        final boolean localTransaction = em.getTransaction().isActive();
        if (!localTransaction) { em.getTransaction().begin(); }
        em.persist(group);
        if (!localTransaction) { em.getTransaction().commit(); }
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

    public List<Group> getGroupsWithAssociatedMetadata(final MetadataCategory metadataCategory, final MetadataValue metadataValue)
    {
        final String queryJQL = "SELECT g FROM Group g JOIN g.metadataAssociations a WHERE a.metadataCategory = :category AND :value MEMBER OF a.metadataValues";
        final TypedQuery<Group> query = em.createQuery(queryJQL, Group.class);
        query.setParameter("category", metadataCategory);
        query.setParameter("value", metadataValue);
        return query.getResultList();
    }

    public List<Group> getGroupsWithAssociatedMetadataAttribute(final MetadataCategory metadataCategory, final MetadataAttributeValue metadataAttributeValue)
    {
        final String queryJQL = "SELECT g FROM Group g JOIN g.metadataAssociations a WHERE a.metadataCategory = :category AND a.metadataAttributeValue = :value";
        final TypedQuery<Group> query = em.createQuery(queryJQL, Group.class);
        query.setParameter("category", metadataCategory);
        query.setParameter("value", metadataAttributeValue);
        return query.getResultList();
    }
    
    /**
     * Returns the list of groups in the hierarchy between the root group
     * and the files in the list of selected files (ie it includes the groups that contain
     * a selected file, and the groups that contain those groups, and the groups that
     * contain those groups, etc)
     * @param selectedFiles The list of selected files
     * @return The list of groups
     */
    public static List<Group> getGroupsContainingSelectedFiles(final List<ResearchFile> selectedFiles)
    {
        List<Group> groups = new ArrayList<Group>(0);
        
        for(final ResearchFile file : selectedFiles)
        {
            Group group = file.getParentGroup();
            
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
                        
                        Group parentGroup = group.getParentGroup();
                        
                        while(parentGroup != null)
                        {
                            if (groups.contains(parentGroup))
                            {
                                break;
                            }
                            else
                            {
                                groups.add(parentGroup);
                                parentGroup = parentGroup.getParentGroup();
                            }
                        }
                    }
                }
                group = group.getParentGroup();
            }
        }
        return groups;
    }
}
