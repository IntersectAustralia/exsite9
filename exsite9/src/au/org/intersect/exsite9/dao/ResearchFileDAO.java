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
	private static ResearchFileDAO instance = null;
	
    private final EntityManager em;
    
    public static ResearchFileDAO getInstance(EntityManager em)
    {
    	if (instance == null)
    	{
    		instance = new ResearchFileDAO(em);
    	}
    	return instance;
    }
    
    public static ResearchFileDAO createTestInstance(EntityManager em)
    {
    	return new ResearchFileDAO(em);
    }
    
    private ResearchFileDAO(EntityManager em)
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
