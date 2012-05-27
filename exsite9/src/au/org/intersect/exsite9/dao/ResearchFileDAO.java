/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.dao;

import javax.persistence.EntityManager;

import au.org.intersect.exsite9.domain.ResearchFile;

public class ResearchFileDAO
{
    private final EntityManager em;
    
    public ResearchFileDAO(EntityManager em)
    {
        this.em = em;
    }
    
    public void createResearchFile(ResearchFile file)
    {
        em.getTransaction().begin();
        em.persist(file);
        em.getTransaction().commit();
    }
}
