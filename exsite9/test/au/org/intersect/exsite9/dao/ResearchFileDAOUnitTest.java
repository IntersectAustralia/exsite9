/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.dao;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.Test;

import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.domain.ResearchFile;

public class ResearchFileDAOUnitTest extends JPATest
{

    private ResearchFileDAO rfm;
    
    @Test
    public void testCreateNewFile()
    {
        EntityManager em = createEntityManager();
        
        File fileOnDisk = new File("some-file.txt");
        ResearchFile researchFile = new ResearchFile(fileOnDisk);
        
        rfm = new ResearchFileDAO(em);
        rfm.createResearchFile(researchFile);
        
        em.close();
    }
    
}
