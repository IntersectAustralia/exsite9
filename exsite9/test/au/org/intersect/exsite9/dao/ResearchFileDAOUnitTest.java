/**
 * Copyright (C) Intersect 2012.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */

package au.org.intersect.exsite9.dao;

import java.io.File;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import au.org.intersect.exsite9.dao.ResearchFileDAO;
import au.org.intersect.exsite9.dao.factory.ResearchFileDAOFactory;
import au.org.intersect.exsite9.domain.ResearchFile;

public class ResearchFileDAOUnitTest extends DAOTest
{

    private static ResearchFileDAOFactory researchFileDAOFactory;
    
    @BeforeClass
    public static void setupOnce()
    {
        researchFileDAOFactory = new ResearchFileDAOFactory();
    }
    
    @Test
    public void testCreateNewFile()
    {
        EntityManager em = createEntityManager();
        ResearchFileDAO researchFileDAO = researchFileDAOFactory.createInstance(em);
        
        File fileOnDisk = new File("some-file.txt");
        ResearchFile researchFile = new ResearchFile(fileOnDisk);
        
        researchFileDAO.createResearchFile(researchFile);
        
        em.close();
    }
    
}
